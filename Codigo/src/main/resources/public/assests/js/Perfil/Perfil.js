document.addEventListener('DOMContentLoaded', async () => {
    // 1. Descobre de quem é o perfil acessado (pega o ID da URL)
    const urlParams = new URLSearchParams(window.location.search);
    let idPerfil = urlParams.get('id');

    const token = localStorage.getItem('token');
    let meuId = null;

    if (token) {
        const decoded = decodeJWT(token); 
        if (decoded) meuId = decoded.sub;
    }

    if (!idPerfil) {
        idPerfil = meuId;
    }

    if (!idPerfil) {
        window.location.href = LOGIN_BASE_URL;
        return;
    }

    if (meuId && idPerfil == meuId) {
        const btnCriarPost = document.getElementById('open-post-modal-btn');
        if (btnCriarPost) {
            btnCriarPost.style.display = 'block';
        }
    }

    // 4. Busca os dados do perfil no back-end
	try {
	        // Busca os dados do usuário (Garante que sua rota Java traga a coluna tipoUsuario)
	        const resposta = await fetch(`${API_BASE_URL}/usuario/${idPerfil}`);

	        if (resposta.ok) {
	            const perfil = await resposta.json();

	            // 1. Preenche as informações básicas (que todo mundo tem)
	            document.getElementById('profile-name').innerText = perfil.nome || "Sem Nome";
	            
	            const fotoElemento = document.getElementById('foto-perfil-img');
	            if (fotoElemento && perfil.foto) {
	                fotoElemento.src = perfil.foto;
	            }

	
	            const isPrestador = (perfil.tipoUsuario && perfil.tipoUsuario.toLowerCase() === 'prestador');

	            if (isPrestador) {

	                document.getElementById('profile-description').innerText = perfil.descricao || "Prestador de serviços.";
	                
	                // Mostra as seções ocultas
	                const secaoPortfolio = document.getElementById('secao-portfolio');
	                if(secaoPortfolio) secaoPortfolio.style.display = 'block';
	                
	                const secaoPlanos = document.getElementById('secao-planos');
	                if(secaoPlanos) secaoPlanos.style.display = 'block';

	                const seloPlano = document.getElementById('profile-plan');
	                if(seloPlano) {
	                    seloPlano.style.display = 'inline-block';
	                    seloPlano.innerText = perfil.plano || "Plano Básico";
	                }

	                // Opcional: Se quiser carregar os posts do prestador a partir daqui
	                if (typeof carregarPostsPorPrestador === "function") {
	                    carregarPostsPorPrestador(idPerfil); 
	                }

	            } else {
	                // ==========================================
	                // SE FOR CLIENTE: ESCONDE COISAS DE PRESTADOR
	                // ==========================================
	                document.getElementById('profile-description').innerText = "Usuário cliente da plataforma.";
	                
	                // Garante que o botão de "Criar Post" suma (mesmo se for o dono do perfil acessando)
	                const btnCriarPost = document.getElementById('open-post-modal-btn');
	                if (btnCriarPost) btnCriarPost.style.display = 'none';
	            }

	        } else {
	            document.getElementById('profile-name').innerText = "Perfil não encontrado";
	        }
	    } catch (error) {
	        console.error("Erro de conexão ao buscar perfil:", error);
	    }
});