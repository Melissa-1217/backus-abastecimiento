document.addEventListener("DOMContentLoaded", () => {
 const grid=document.querySelector(".product-grid"); if(!grid) return;
 const cards=[...grid.querySelectorAll(".product-card")];
 const ids=["filtro-categoria","filtro-marca","filtro-presentacion","filtro-estado","solo-disponibles","orden"];
 const controls=Object.fromEntries(ids.map(id=>[id,document.getElementById(id)]));
 for(const id of ids.slice(0,3)){const seen=new Set();[...controls[id].options].forEach(o=>{if(seen.has(o.value))o.remove();else seen.add(o.value);});}
 const categoriaInicial=new URLSearchParams(window.location.search).get("categoria");
 if(categoriaInicial && [...controls[ids[0]].options].some(o=>o.value===categoriaInicial)) controls[ids[0]].value=categoriaInicial;
 function update(){
 document.querySelectorAll(".category-pill").forEach(b=>{
 const selected=b.dataset.category===controls[ids[0]].value;
 b.classList.toggle("selected",selected);
 b.setAttribute("aria-pressed",String(selected));
 });
 let count=0; cards.forEach(card=>{const d=card.dataset,q=Number(d.cantidad),state=q===0?"agotado":q<=5?"bajo":"disponible";const ok=(!controls[ids[0]].value||d.categoria===controls[ids[0]].value)&&(!controls[ids[1]].value||d.marca===controls[ids[1]].value)&&(!controls[ids[2]].value||d.presentacion===controls[ids[2]].value)&&(!controls[ids[3]].value||state===controls[ids[3]].value)&&(!controls[ids[4]].checked||q>0);card.hidden=!ok;if(ok)count++;});
 cards.sort((a,b)=>controls.orden.value==="precio"?Number(a.dataset.precio)-Number(b.dataset.precio):a.dataset.nombre.localeCompare(b.dataset.nombre,"es")).forEach(c=>grid.append(c));
 document.getElementById("contador-resultados").textContent=`Mostrando ${count} presentaciones`;
 document.getElementById("sin-resultados-filtros").hidden=count>0||cards.length===0;
 }
 Object.values(controls).forEach(c=>c.addEventListener("change",update));
 document.querySelectorAll(".category-pill").forEach(b=>b.addEventListener("click",()=>{controls[ids[0]].value=b.dataset.category;document.querySelectorAll(".category-pill").forEach(x=>x.classList.toggle("selected",x===b));update();}));update();
});
