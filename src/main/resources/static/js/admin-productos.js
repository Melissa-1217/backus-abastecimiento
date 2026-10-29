document.addEventListener("DOMContentLoaded",()=>{
 const rows=[...document.querySelectorAll(".product-admin-row")],search=document.querySelector("#producto-buscar"),category=document.querySelector("#producto-categoria"),state=document.querySelector("#producto-estado");
 function filter(){const q=(search?.value||"").trim().toLocaleLowerCase("es");let count=0;rows.forEach(row=>{const match=row.dataset.search.toLocaleLowerCase("es").includes(q)&&(!category.value||row.dataset.category===category.value)&&(!state.value||row.dataset.state===state.value);row.hidden=!match;if(match)count++;});const label=document.querySelector("[data-products-count]");if(label)label.textContent=`Mostrando ${count} de ${rows.length} productos`;const empty=document.querySelector("[data-no-products]");if(empty)empty.hidden=count!==0||rows.length===0;}
 search?.addEventListener("input",filter);category?.addEventListener("change",filter);state?.addEventListener("change",filter);document.querySelector("[data-reset-products]")?.addEventListener("click",()=>{search.value="";category.value="";state.value="";filter();});if(search)filter();
 const input=document.querySelector("[data-image-input]"),image=document.querySelector("[data-image-preview]"),error=document.querySelector("[data-image-error]"),file=document.querySelector("[data-image-file]");let objectUrl;
 function preview(){if(objectUrl){URL.revokeObjectURL(objectUrl);objectUrl=null;}error.hidden=true;image.hidden=true;const selected=file?.files?.[0];if(selected){if(selected.size>5*1024*1024){error.textContent="La imagen no debe superar 5 MB.";error.hidden=false;file.setCustomValidity("Selecciona una imagen de hasta 5 MB.");return;}file.setCustomValidity("");objectUrl=URL.createObjectURL(selected);image.src=objectUrl;}else{file?.setCustomValidity("");const url=input.value.trim();if(url.startsWith("https://")||url.startsWith("/media/productos/"))image.src=url;}}
 if(input){image.addEventListener("load",()=>image.hidden=false);image.addEventListener("error",()=>{image.hidden=true;error.textContent="No se pudo cargar la imagen. Revisa el enlace o archivo.";error.hidden=false;});input.addEventListener("change",preview);file?.addEventListener("change",preview);preview();}
});

document.addEventListener("DOMContentLoaded",()=>{
 const cards=document.querySelector("[data-gallery-cards]"),add=document.querySelector("[data-add-photo]"),links=document.querySelector("[data-gallery-links]");if(!cards)return;
 let sequence=0;
 add.addEventListener("click",()=>{
  const card=document.createElement("article");card.className="new-photo-card";
  const frame=document.createElement("div");frame.className="gallery-photo-frame";
  const image=document.createElement("img");image.alt="Vista previa de la nueva foto";image.hidden=true;frame.append(image);
  const placeholder=document.createElement("span");placeholder.className="photo-placeholder";placeholder.textContent="Nueva imagen";frame.append(placeholder);
  const file=document.createElement("input");file.type="file";file.name="fotosArchivos";file.accept="image/png,image/jpeg";file.id="foto-archivo-"+(++sequence);file.className="gallery-file-input";
  const choose=document.createElement("label");choose.htmlFor=file.id;choose.className="choose-photo-file";choose.textContent="Seleccionar archivo";
  const url=document.createElement("input");url.type="url";url.placeholder="O pega un enlace HTTPS";url.className="gallery-url-input";url.dataset.newPhotoUrl="";url.maxLength=1000;
  const remove=document.createElement("button");remove.type="button";remove.className="remove-new-photo";remove.textContent="Quitar";
  let objectUrl;
  function preview(){if(objectUrl){URL.revokeObjectURL(objectUrl);objectUrl=null;}image.hidden=true;placeholder.hidden=false;file.setCustomValidity("");url.setCustomValidity("");const selected=file.files[0];if(selected){if(selected.size>5*1024*1024){file.setCustomValidity("El archivo debe pesar hasta 5 MB.");file.reportValidity();return;}objectUrl=URL.createObjectURL(selected);image.src=objectUrl;url.disabled=true;url.value="";choose.textContent="Cambiar archivo";}else{url.disabled=false;choose.textContent="Seleccionar archivo";const value=url.value.trim();if(value&&!value.startsWith("https://")){url.setCustomValidity("Usa un enlace HTTPS.");return;}if(value)image.src=value;}}
  image.addEventListener("load",()=>{image.hidden=false;placeholder.hidden=true;});image.addEventListener("error",()=>{image.hidden=true;placeholder.hidden=false;placeholder.textContent="Revisa la imagen o el enlace";});
  file.addEventListener("change",preview);url.addEventListener("input",preview);remove.addEventListener("click",()=>{if(objectUrl)URL.revokeObjectURL(objectUrl);card.remove();});
  card.append(frame,file,choose,url,remove);cards.insertBefore(card,add);choose.focus();
 });
 cards.closest("form").addEventListener("submit",()=>{links.value=[...cards.querySelectorAll("[data-new-photo-url]")].filter(i=>!i.disabled).map(i=>i.value.trim()).filter(Boolean).join("\n");});
 cards.querySelectorAll(".remove-photo-choice input").forEach(check=>check.addEventListener("change",()=>{const card=check.closest("article");card.classList.toggle("photo-to-remove",check.checked);const radio=card.querySelector('input[type="radio"]');radio.disabled=check.checked;if(check.checked)radio.checked=false;}));
});
