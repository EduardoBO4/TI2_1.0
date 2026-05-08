const mockJSON = {
  posts: [
    {
      foto: "https://images.unsplash.com/photo-1621905251189-08b45d6a269e?w=800&q=80",
      titulo: "Troca de Fiação",
      legenda: "Serviço completo de troca de fiação em residência antiga."
    },
    {
      foto: "https://images.unsplash.com/photo-1581092921461-eab62e97a780?w=800&q=80",
      titulo: "Instalação de Painel",
      legenda: "Quadro de distribuição montado e organizado."
    }
  ]
};

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
    // Pegamos o resultado original (com data:image/...)
    let fullBase64 = e.target.result;
    
    // Aplicamos a limpeza que você enviou:
    // 1. Remove "data:"
    // 2. Remove tudo até a primeira vírgula (o cabeçalho do tipo de arquivo)
    let base64Limpo = fullBase64.replace("data:", "").replace(/^.+,/, "");
    
    // imagemSelecionada agora terá apenas a string pura
    imagemSelecionada = base64Limpo;
    
    // Para o PREVIEW funcionar no navegador, precisamos do Base64 COMPLETO.
    // Então passamos o 'fullBase64' para o src da imagem.
    document.getElementById('pm-preview-img').src = fullBase64;
    document.getElementById('pm-preview-wrap').style.display = 'block';
    document.getElementById('pm-drop-area').style.display = 'none';
    
    console.log("Base64 Limpo:", imagemSelecionada);
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

function processarArquivo(file) {
  if (!file || !file.type.startsWith('image/')) return;
  const reader = new FileReader();
  reader.onload = (e) => {
    imagemSelecionada = e.target.result;
    document.getElementById('pm-preview-img').src = imagemSelecionada;
    document.getElementById('pm-preview-wrap').style.display = 'block';
    document.getElementById('pm-drop-area').style.display = 'none';
  };
  reader.readAsDataURL(file);
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
async function carregarPosts() {
  const postsList = document.getElementById('portfolio-posts-list');
  const galleryGrid = document.getElementById('portfolio-grid');
  
  let posts = [];
  try {
    const resposta = await fetch(`${API_BASE_URL}/posts`);
    posts = resposta.ok ? await resposta.json() : mockJSON.posts;
  } catch (e) {
    posts = mockJSON.posts;
  }

  postsList.innerHTML = '';
  galleryGrid.innerHTML = '';

  posts.forEach(post => {
    // Usando .foto conforme seu banco de dados
    const imgHtml = post.foto ? `<img src="${post.foto}" class="post-img">` : '';
    postsList.innerHTML += `
      <div class="post-item">
        ${imgHtml}
        <div class="post-content">
          <h4>${post.titulo || 'Serviço'}</h4>
          <p>${post.legenda || ''}</p>
        </div>
      </div>`;
    if (post.foto) {
      galleryGrid.innerHTML += `<img src="${post.foto}" class="gallery-img">`;
    }
  });
}

document.getElementById('pm-publish-btn').addEventListener('click', async () => {
  const titulo = document.getElementById('pm-titulo').value.trim();
  const desc = document.getElementById('pm-desc').value.trim();
  const tagClient = document.getElementById('pm-client-tag').value.trim();

  if (!desc) return alert('A descrição é obrigatória!');

  let imagemFinal = "";

  if (imagemSelecionada) {
    if (imagemSelecionada.startsWith('data:')) {
      imagemFinal = imagemSelecionada;
    } else {
      try {
        imagemFinal = await urlParaBase64(imagemSelecionada);
      } catch (e) {
        console.warn("CORS ou erro na conversão. Usando URL original.");
        imagemFinal = imagemSelecionada; 
      }
    }
  }

  const novoPost = {
	idprestador: (() => {
	    const token = localStorage.getItem("token");
	    if (!token) return 0;
	    const decoded = decodeJWT(token);
	    return decoded ? parseInt(decoded.sub) : 0;
	})(),
    titulo: titulo || (tagClient ? `Atendimento para @${tagClient}` : 'Novo Serviço'),
    legenda: desc,
    foto: imagemFinal 
  };
  
  

  // DEBUG: Veja se o campo 'foto' está preenchido aqui no console
  console.log("Objeto enviado para o Back-end:", novoPost);

  try {
    const resposta = await fetch(`${API_BASE_URL}/post`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(novoPost)
    });

    // Se o back-end retornar erro de "Payload Too Large" (413), 
    // você saberá que é o limite do Express (Dica #1)
    if (resposta.status === 413) {
      alert("Erro: A imagem é muito grande para o servidor!");
      return;
    }

    if (resposta.status === 201 || resposta.ok) {
      alert('Publicado com sucesso!');
      fecharModal();
      carregarPosts();
    } else {
      const errorData = await resposta.json();
      console.error("Erro retornado pelo Back-end:", errorData);
      throw new Error();
    }
  } catch (e) {
    console.warn("Back-end offline ou erro de conexão. Salvando no Mock.");
    mockJSON.posts.unshift(novoPost);
    fecharModal();
    carregarPosts();
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
  carregarPosts();
  
  document.getElementById('open-post-modal-btn').addEventListener('click', abrirModal);
  document.getElementById('close-post-modal').addEventListener('click', fecharModal);
  document.getElementById('pm-cancel-btn').addEventListener('click', fecharModal);
  document.getElementById('pm-file-input').addEventListener('change', (e) => processarArquivo(e.target.files[0]));
  
  let urlDebounce;
  document.getElementById('pm-img-url').addEventListener('input', (e) => {
    clearTimeout(urlDebounce);
    const url = e.target.value.trim();
    if (url) urlDebounce = setTimeout(() => mostrarPreviewURL(url), 600);
  });
});