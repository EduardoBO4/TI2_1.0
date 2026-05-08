let imagemSelecionada = null; 
let tabAtiva = 'upload';

// ==========================================
// FUNÇÕES DE AUXÍLIO E CONVERSÃO
// ==========================================
function renderStars(rating) {
  const container = document.getElementById('stars-display');
  if (!container) return;
  container.innerHTML = '';
  for (let i = 1; i <= 5; i++) {
    const star = document.createElement('i');
    star.className = i <= Math.round(rating) ? 'fa-solid fa-star' : 'fa-regular fa-star';
    container.appendChild(star);
  }
}

function processarArquivo(file) {
  if (!file || !file.type.startsWith('image/')) return;
  
  const reader = new FileReader();
  
  reader.onload = (e) => {
    // CORREÇÃO 1: Mantemos o Base64 original com o prefixo "data:image/..."
    let fullBase64 = e.target.result;
    
    imagemSelecionada = fullBase64;
    
    document.getElementById('pm-preview-img').src = fullBase64;
    document.getElementById('pm-preview-wrap').style.display = 'block';
    document.getElementById('pm-drop-area').style.display = 'none';
  };
  reader.readAsDataURL(file);
}

// ==========================================
// GESTÃO DO MODAL E PREVIEW
// ==========================================
function switchTab(tab) {
  tabAtiva = tab;
  document.getElementById('panel-upload').style.display = tab === 'upload' ? 'block' : 'none';
  document.getElementById('panel-url').style.display = tab === 'url' ? 'block' : 'none';
  document.getElementById('tab-upload').classList.toggle('active', tab === 'upload');
  document.getElementById('tab-url').classList.toggle('active', tab === 'url');
  tab === 'upload' ? limparURL() : limparUpload();
}

function limparUpload() {
  imagemSelecionada = null;
  document.getElementById('pm-preview-img').src = '';
  document.getElementById('pm-preview-wrap').style.display = 'none';
  document.getElementById('pm-drop-area').style.display = 'flex';
  document.getElementById('pm-file-input').value = '';
}

function limparURL() {
  imagemSelecionada = null;
  document.getElementById('pm-img-url').value = '';
  document.getElementById('pm-preview-img-url').src = '';
  document.getElementById('pm-preview-wrap-url').style.display = 'none';
}

function mostrarPreviewURL(url) {
  imagemSelecionada = url;
  const img = document.getElementById('pm-preview-img-url');
  const wrap = document.getElementById('pm-preview-wrap-url');
  img.src = url;
  img.onload = () => { wrap.style.display = 'block'; };
  img.onerror = () => {
    wrap.style.display = 'none';
    imagemSelecionada = null;
  };
}

// ==========================================
// CARREGAR E PUBLICAR (CAMPO 'FOTO')
// ==========================================
async function carregarPostsPorPrestador() {
  const postsList = document.getElementById('portfolio-posts-list');
  const galleryGrid = document.getElementById('portfolio-grid');
  
  const urlParams = new URLSearchParams(window.location.search);
  let idPrestador = urlParams.get('id');

  if (!idPrestador) {
    const token = localStorage.getItem("token");
    if (token) {
      const decoded = decodeJWT(token);
      idPrestador = decoded.sub;
    }
  }

  if (!idPrestador) return; 

  let posts = [];
  try {
    const resposta = await fetch(`${API_BASE_URL}/posts/prestador/${idPrestador}`);
    if (resposta.ok) {
        posts = await resposta.json();
    }
  } catch (e) {
    console.error("Erro ao carregar posts:", e);
  }

  if(postsList) postsList.innerHTML = '';
  if(galleryGrid) galleryGrid.innerHTML = '';

  posts.forEach(post => {
    // CORREÇÃO 2: Trava de segurança para posts antigos
    let imgSrc = post.foto;
    if (imgSrc && !imgSrc.startsWith('http') && !imgSrc.startsWith('data:')) {
        imgSrc = 'data:image/jpeg;base64,' + imgSrc;
    }

    const imgHtml = imgSrc ? `<img src="${imgSrc}" class="post-img">` : '';
    
    if(postsList) {
        postsList.innerHTML += `
        <div class="post-item">
            ${imgHtml}
            <div class="post-content">
            <h4>${post.titulo || 'Serviço'}</h4>
            <p>${post.legenda || ''}</p>
            </div>
        </div>`;
    }
    if (galleryGrid && imgSrc) {
      galleryGrid.innerHTML += `<img src="${imgSrc}" class="gallery-img">`;
    }
  });
}

document.getElementById('pm-publish-btn').addEventListener('click', async () => {
  const titulo = document.getElementById('pm-titulo').value.trim();
  const desc = document.getElementById('pm-desc').value.trim();

  if (!desc) return alert('A descrição é obrigatória!');

  // imagemSelecionada já tem o formato completo (Base64 longo ou URL HTTP)
  let imagemFinal = imagemSelecionada ? imagemSelecionada : "";

  const token = localStorage.getItem("token");
  const decoded = token ? decodeJWT(token) : null;
  const meuId = decoded ? parseInt(decoded.sub) : 0;

  const novoPost = {
    idprestador: meuId,
    titulo: titulo,
    legenda: desc,
    foto: imagemFinal 
  };

  try {
    const resposta = await fetchAutenticado(`${API_BASE_URL}/post`, {
      method: 'POST',
      body: JSON.stringify(novoPost)
    });

    if (!resposta) return; 

    if (resposta.status === 413) {
      alert("Erro: A imagem é muito grande para o servidor!");
      return;
    }

    if (resposta.status === 201 || resposta.ok) {
      alert('Publicado com sucesso!');
      fecharModal();
      carregarPostsPorPrestador(); 
    } else {
      const errorData = await resposta.json();
      alert("Erro ao publicar: " + errorData.mensagem);
    }
  } catch (e) {
      console.error("Erro fatal ao criar post:", e);
      alert("Erro de conexão com o servidor.");
  }
});

// ==========================================
// INICIALIZAÇÃO E EVENTOS DE UI
// ==========================================
function fecharModal() { document.getElementById('post-modal-overlay').style.display = 'none'; }
function abrirModal() {
  limparUpload(); limparURL(); switchTab('upload');
  document.getElementById('pm-titulo').value = '';
  document.getElementById('pm-desc').value = '';
  document.getElementById('post-modal-overlay').style.display = 'flex';
}

document.addEventListener('DOMContentLoaded', () => {
  renderStars(4.25);
  carregarPostsPorPrestador();
  
  const openModalBtn = document.getElementById('open-post-modal-btn');
  if (openModalBtn) openModalBtn.addEventListener('click', abrirModal);
  
  document.getElementById('close-post-modal').addEventListener('click', fecharModal);
  document.getElementById('pm-cancel-btn').addEventListener('click', fecharModal);
  
  const fileInput = document.getElementById('pm-file-input');
  if (fileInput) fileInput.addEventListener('change', (e) => processarArquivo(e.target.files[0]));
  
  let urlDebounce;
  const urlInput = document.getElementById('pm-img-url');
  if (urlInput) {
      urlInput.addEventListener('input', (e) => {
        clearTimeout(urlDebounce);
        const url = e.target.value.trim();
        if (url) urlDebounce = setTimeout(() => mostrarPreviewURL(url), 600);
      });
  }
});