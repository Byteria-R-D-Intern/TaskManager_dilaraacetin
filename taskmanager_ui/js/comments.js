(function(){
  const API_BASE = "http://localhost:8080/api";
  const token = localStorage.getItem("jwtToken");
  if (!token) { return; }

  const style = document.createElement("style");
  style.textContent = `
  .c-mask{position:fixed; inset:0; background:rgba(0,0,0,.28); opacity:0; pointer-events:none; transition:opacity .18s ease; z-index:80;}
  .c-drawer{position:fixed; top:0; right:-520px; width:520px; max-width:95vw; height:100vh; background:#fff; border-left:1px solid var(--border);
            box-shadow: -8px 0 24px rgba(18,38,63,.08); z-index:90; display:flex; flex-direction:column; transition:right .22s ease;}
  .c-open .c-drawer{ right:0; }
  .c-open .c-mask{ opacity:1; pointer-events:auto; }
  .c-head{ padding:12px 14px; border-bottom:1px dashed var(--border); display:flex; justify-content:space-between; align-items:center; gap:10px; }
  .c-body{ padding:12px 14px; flex:1; overflow:auto; display:grid; gap:10px; }
  .c-foot{ padding:12px 14px; border-top:1px dashed var(--border); display:flex; gap:8px; }
  .c-item{ border:1px solid var(--border); background:#fff; border-radius:12px; padding:10px; }
  .c-meta{ font-size:12px; color:var(--muted); display:flex; gap:8px; flex-wrap:wrap; }
  .c-text{ margin-top:6px; line-height:1.4; white-space:pre-wrap; }
  .c-empty{ color:var(--muted); text-align:center; padding:18px; border:1px dashed var(--border); border-radius:12px; }
  textarea.c-input{ width:100%; min-height:88px; border-radius:10px; padding:10px 12px; border:1px solid var(--border); outline:none; }
  textarea.c-input:focus{ border-color:var(--ring); box-shadow:0 0 0 3px rgba(141,165,248,.25); }
  `;
  document.head.appendChild(style);

  const mask = document.createElement("div");
  mask.className = "c-mask";
  const drawer = document.createElement("aside");
  drawer.className = "c-drawer";
  drawer.innerHTML = `
    <div class="c-head">
      <div class="brand" id="c-title" style="font-weight:700;">Yorumlar</div>
      <button class="btn" id="c-close">Kapat</button>
    </div>
    <div class="c-body">
      <div id="c-list"></div>
      <div id="c-empty" class="c-empty" style="display:none;">Henüz yorum yok.</div>
    </div>
    <div class="c-foot">
      <textarea id="c-input" class="c-input" placeholder="Yorum yaz..."></textarea>
      <button id="c-send" class="btn primary">Gönder</button>
    </div>
  `;
  document.body.appendChild(mask);
  document.body.appendChild(drawer);

  let open = false;
  let currentTaskId = null;
  function show(){
    document.documentElement.classList.add("c-open");
    open = true;
  }
  function hide(){
    document.documentElement.classList.remove("c-open");
    open = false; currentTaskId = null;
  }
  mask.addEventListener("click", hide);
  drawer.querySelector("#c-close").addEventListener("click", hide);
  document.addEventListener("keydown", (e)=>{ if (open && e.key==="Escape") hide(); });

  function esc(s){ if(!s) return ""; return String(s).replaceAll("&","&amp;").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll('"',"&quot;").replaceAll("'","&#039;"); }
  const fmt = ts => { try { return ts ? new Date(ts).toLocaleString('tr-TR') : ""; } catch { return ts||""; } };

  async function authFetch(url, options = {}){
    const headers = options.headers || {};
    const res = await fetch(url, {
      ...options,
      headers: { ...headers, "Authorization": `Bearer ${token}`, ...(options.method && options.method!=="GET" ? {"Content-Type":"application/json"} : {}) }
    });
    if (res.status === 401 || res.status === 403){
      alert("Yetki yok / oturum süresi doldu.");
      return res;
    }
    return res;
  }

  async function loadComments(taskId){
    const listEl = drawer.querySelector("#c-list");
    const emptyEl = drawer.querySelector("#c-empty");
    listEl.innerHTML = "";
    emptyEl.style.display = "none";
    try{
      const res = await authFetch(`${API_BASE}/tasks/${encodeURIComponent(taskId)}/comments`);
      if (!res.ok){ throw new Error(await res.text()); }
      const arr = await res.json();
      if (!Array.isArray(arr) || arr.length===0){
        emptyEl.style.display = "";
        return;
      }
      for (const c of arr){
        const div = document.createElement("div");
        const text = c.content ?? c.text ?? c.message ?? "";
        const username = c.username ?? c.authorUsername ?? ("user#" + (c.userId ?? c.authorUserId ?? "-"));
        const createdAt = c.createdAt ?? c.timestamp ?? c.created_at ?? null;
        div.className = "c-item";
        div.innerHTML = `
          <div class="c-meta">@${esc(username)} • <span>${esc(fmt(createdAt))}</span></div>
          <div class="c-text">${esc(text)}</div>
        `;
        listEl.appendChild(div);
      }
    }catch(e){
      console.error("comments load error:", e);
      emptyEl.style.display = "";
      emptyEl.textContent = "Yorumlar alınamadı.";
    }
  }

  async function sendComment(taskId, text){
    const payload = { content: text }; 
    const res = await authFetch(`${API_BASE}/tasks/${encodeURIComponent(taskId)}/comments`, {
      method:"POST",
      body: JSON.stringify(payload)
    });
    if (!res.ok){
      const msg = (await res.text().catch(()=> "")) || "Yorum eklenemedi.";
      throw new Error(msg);
    }
    return res.json().catch(()=> ({}));
  }

  drawer.querySelector("#c-send").addEventListener("click", async ()=>{
    if (!currentTaskId) return;
    const inp = drawer.querySelector("#c-input");
    const text = inp.value.trim();
    if (!text) return;
    try{
      await sendComment(currentTaskId, text);
      inp.value = "";
      await loadComments(currentTaskId);
    }catch(e){
      alert(e.message || "Yorum gönderilemedi.");
    }
  });

  window.openCommentsDrawer = function(taskId, taskTitle){
    currentTaskId = taskId;
    drawer.querySelector("#c-title").textContent = `Yorumlar • #${taskId} – ${taskTitle||""}`;
    drawer.querySelector("#c-input").value = "";
    show();
    loadComments(taskId);
  };
  window.closeCommentsDrawer = hide;
})();
