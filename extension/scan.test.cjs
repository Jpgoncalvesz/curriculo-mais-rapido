const {test}=require('node:test');
const assert=require('node:assert/strict');
const vm=require('node:vm');
const fs=require('node:fs');
function scan(fields,profile){const elements={};const document={getElementById(id){return elements[id]||=( {} );},querySelectorAll(){return fields;}};const context=vm.createContext({document,chrome:{storage:{local:{get:async()=>({})}}}});vm.runInContext(fs.readFileSync(__dirname+'/popup.js','utf8'),context);return JSON.parse(JSON.stringify(context.scanPage(profile)));}
function field(label,type='text',opts={}){return {labels:[{textContent:label}],type,disabled:false,readOnly:false,value:'',getClientRects:()=>[{}],getAttribute:()=>'',...opts};}
test('mapeia contatos e confirmação de email sem escolher arquivos ou senhas',()=>{const result=scan([field('Nome completo'),field('Confirmar e-mail','email'),field('Senha','password'),field('Currículo','file'),field('LinkedIn','url')],{name:'Ana Silva',email:'ana@example.com',linkedin:'linkedin.com/in/ana'});assert.equal(result.length,3);assert.equal(result[0].value,'Ana Silva');assert.equal(result[1].value,'ana@example.com');assert.equal(result[2].key,'linkedin');});
test('ignora campos invisíveis, desabilitados e somente leitura',()=>{assert.equal(scan([field('Nome completo','text',{disabled:true}),field('Email','email',{readOnly:true}),field('Telefone','tel',{getClientRects:()=>[]})],{name:'Ana',email:'a@example.com',phone:'11999999999'}).length,0);});
test('separa primeiro nome e sobrenome e preserva informação sobre valor existente',()=>{const result=scan([field('Primeiro nome'),field('Sobrenome','text',{value:'Já preenchido'})],{name:'Ana Maria Silva'});assert.equal(result[0].value,'Ana');assert.equal(result[1].value,'Maria Silva');assert.equal(result[1].current,'Já preenchido');});
