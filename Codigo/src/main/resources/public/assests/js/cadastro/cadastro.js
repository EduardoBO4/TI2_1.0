async function doRegister(tipo) {
    const nome   = document.getElementById("inp-nome").value.trim();
    const email  = document.getElementById("inp-email").value.trim();
    const senha  = document.getElementById("inp-pw").value;
    const senha2 = document.getElementById("inp-pw2").value;
    const errEl  = document.getElementById("error-msg");

    if (!nome || !email || !senha || !senha2) {
        errEl.textContent = 'Preencha todos os campos.';
        errEl.style.display = 'block';
        return;
    }
	
	function togglePw(id, btn) {
	    const inp = document.getElementById(id);
	    const isHidden = inp.type === 'password';
	    
	    inp.type = isHidden ? 'text' : 'password';
	    
	    if (isHidden) {
	        btn.innerHTML = `<svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path><line x1="1" y1="1" x2="23" y2="23"></line></svg>`;
	    } else { 
	        btn.innerHTML = `<svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>`;
	    }
	}

    if (senha !== senha2) {
        errEl.textContent = 'As senhas não coincidem.';
        errEl.style.display = 'block';
        return;
    }

    if (senha.length < 6) {
        errEl.textContent = 'A senha deve ter pelo menos 6 caracteres.';
        errEl.style.display = 'block';
        return;
    }

    errEl.style.display = 'none';

    if (tipo === 'prestador') {
        sessionStorage.setItem('profeed_reg_temp', JSON.stringify({ nome, email, senha }));
        window.location.href = '/modulos/cadastro-prestador.html';
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/cadastro`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nome, email, senha, tipoUsuario: tipo })
        });

        const data = await response.json();

        if (response.ok) {
            alert("Cadastro realizado com sucesso!");
            window.location.href = "/modulos/login.html";
        } else {
            errEl.textContent = data.mensagem || "Erro ao cadastrar.";
            errEl.style.display = 'block';
        }
    } catch (error) {
        alert("Erro na conexão. Verifique se o servidor está rodando.");
    }
}