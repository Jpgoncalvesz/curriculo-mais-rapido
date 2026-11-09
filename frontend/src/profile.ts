export type Profile = {name:string;email:string;phone:string;city:string;linkedin:string;role:string;summary:string;experience:string;education:string;skills:string};
export const empty:Profile={name:'',email:'',phone:'',city:'',linkedin:'',role:'',summary:'',experience:'',education:'',skills:''};
export function extract(text:string):Partial<Profile>{
 const lines=text.split(/\n/).map(s=>s.trim()).filter(Boolean);
 const section=(names:string[],stop:string[])=>{const start=lines.findIndex(l=>names.some(n=>l.toLocaleLowerCase('pt-BR').startsWith(n)));if(start<0)return '';let end=lines.findIndex((l,i)=>i>start&&stop.some(n=>l.toLocaleLowerCase('pt-BR').startsWith(n)));return lines.slice(start+1,end<0?undefined:end).join('\n');};
 const headers=['experiência','experiencia','formação','formacao','educação','habilidades','competências','resumo','objetivo','idiomas','cursos'];
 return {name:lines.find(l=>/^[\p{L} .'-]{5,80}$/u.test(l)&&!(/^(curr[ií]culo|curriculum|resume|dados pessoais|informa[çc][oõ]es pessoais)\b/i.test(l))&&!headers.some(h=>l.toLowerCase().startsWith(h)))||'',email:text.match(/[\w.+-]+@[\w.-]+\.[a-zA-Z]{2,}/)?.[0]||'',phone:text.match(/(?:\+55\s*)?\(?\d{2}\)?[\s.-]*\d{4,5}[\s.-]*\d{4}/)?.[0]||'',linkedin:text.match(/(?:https?:\/\/)?(?:www\.)?linkedin\.com\/in\/[\w%-]+\/?/i)?.[0]||'',summary:section(['resumo','objetivo'],headers),experience:section(['experiência','experiencia'],headers),education:section(['formação','formacao','educação'],headers),skills:section(['habilidades','competências'],headers)};
}
export function validate(p:Profile){return !!p.name.trim()&&/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(p.email);}
