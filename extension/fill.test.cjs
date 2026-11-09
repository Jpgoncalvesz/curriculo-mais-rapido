const {test}=require('node:test');
const assert=require('node:assert/strict');
const vm=require('node:vm');
const fs=require('node:fs');
class Input {
 constructor(label,type='text'){this.tagName='INPUT';this.type=type;this.name=label;this.id=label;this.labels=[{textContent:label}];this.disabled=false;this.readOnly=false;this._value='';this.events=[];}
 get value(){return this._value} set value(v){this._value=v}
 getClientRects(){return [{}]} getAttribute(){return ''} dispatchEvent(e){this.events.push(e.type)}
}
function setup(){const field=new Input('Email','email');let fields=[field];const elements={};const context=vm.createContext({document:{getElementById:id=>elements[id]||=( {} ),querySelectorAll:()=>fields},chrome:{storage:{local:{get:async()=>({})}}},HTMLInputElement:Input,HTMLTextAreaElement:Input,Event:class {constructor(type){this.type=type}}});vm.runInContext(fs.readFileSync(__dirname+'/popup.js','utf8'),context);const matches=context.scanPage({email:'ana@example.com'});return {field,context,matches,replace:f=>fields=f};}
test('preenche apenas campos revisados e emite os eventos do formulário',()=>{const {field,context,matches}=setup();assert.equal(context.fillPage(matches),1);assert.equal(field.value,'ana@example.com');assert.deepEqual(field.events,['input','change']);});
test('não sobrescreve valor alterado depois da revisão',()=>{const {field,context,matches}=setup();field.value='outro@example.com';assert.equal(context.fillPage(matches),0);assert.equal(field.value,'outro@example.com');});
test('não preenche outro campo movido para a mesma posição',()=>{const {context,matches,replace}=setup();const phone=new Input('Telefone','tel');replace([phone]);assert.equal(context.fillPage(matches),0);assert.equal(phone.value,'');});
test('não preenche campo bloqueado após a identificação',()=>{const {field,context,matches}=setup();field.readOnly=true;assert.equal(context.fillPage(matches),0);assert.equal(field.value,'');});
