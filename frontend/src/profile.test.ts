import {describe,it,expect} from 'vitest';
import {extract,validate,empty} from './profile';
describe('variações de currículo',()=>{
 it('ignora o título do documento ao reconhecer o nome',()=>{expect(extract('CURRÍCULO VITAE\nDados pessoais\nAna Maria Silva\nana@example.com').name).toBe('Ana Maria Silva');});
 it('aceita acentos, apóstrofos e telefone internacional',()=>{const p=extract("João D'Ávila\n+55 (21) 99999-8888\nlinkedin.com/in/joao-davila");expect(p.name).toBe("João D'Ávila");expect(p.phone).toBe('+55 (21) 99999-8888');expect(p.linkedin).toBe('linkedin.com/in/joao-davila');});
 it('não fabrica dados para texto vazio',()=>{const p=extract('');expect(Object.values(p).every(v=>v==='')).toBe(true);});
 it('rejeita email incompleto',()=>{expect(validate({...empty,name:'Ana',email:'ana@'})).toBe(false);});
});
describe('importação e revisão',()=>{it('extrai contatos e separa seções sem inventar dados',()=>{const p=extract('Ana Silva\nana@example.com\n(11) 99999-8888\nExperiência\nAnalista na Empresa\nFormação\nAdministração\nHabilidades\nExcel');expect(p.name).toBe('Ana Silva');expect(p.email).toBe('ana@example.com');expect(p.experience).toBe('Analista na Empresa');expect(p.education).toBe('Administração');expect(p.skills).toBe('Excel');expect(p.linkedin).toBe('');});it('exige nome e email válido para exportar',()=>{expect(validate(empty)).toBe(false);expect(validate({...empty,name:'Ana',email:'ana@example.com'})).toBe(true);});});
