const formulariosDemonstrativos = [
    [
        '.contato-formulario', 'Mensagem registrada. Nenhum e-mail foi enviado.',
        ['nome', 'email', 'assunto', 'mensagem']
    ],
    [
        '.recuperar-formulario',
        'Se o e-mail estiver cadastrado, as instruções aparecerão aqui. Nenhum e-mail foi enviado.',
        ['email']
    ],
    [
        '.login-formulario:not(.recuperar-formulario)', 'Confira seu e-mail e senha.',
        ['email', 'senha']
    ],
    [
        '.cadastro-tutor-formulario', 'Confira seus dados para criar a conta.',
        ['nome', 'email', 'telefone', 'senha', 'confirmar-senha', 'termos']
    ],
    [
        '.cadastro-profissional-formulario', 'Confira seus dados profissionais.',
        [
            'nome', 'email', 'telefone', 'regiao', 'servico', 'apresentacao', 'senha',
            'confirmar-senha', 'termos'
        ]
    ],
    ['.cadastro-pet-formulario', 'Preencha os dados do pet.', ['nome', 'raca']],
    ['.editar-perfil-formulario', 'Confira os dados do perfil.', ['nome', 'email']],
    ['.perfil-tutor-formulario', 'Confira seus dados de contato.', ['nome', 'email']],
    [
        '.editar-profissional-formulario', 'Confira seus dados profissionais.',
        ['nome', 'email', 'telefone', 'regiao', 'sobre']
    ],
    ['.novo-registro-formulario', 'Preencha o título e o relato.', ['titulo', 'relato']]
];

function mostrarAviso(alvo, texto, classe = 'aviso-interacao') {
    let aviso = alvo.querySelector(`.${classe}`);
    if (!aviso) {
        aviso = document.createElement('p');
        aviso.className = classe;
        aviso.setAttribute('role', 'status');
        aviso.setAttribute('aria-live', 'polite');
        alvo.append(aviso);
    }
    aviso.textContent = texto;
}

formulariosDemonstrativos.forEach(([seletor, mensagem, campos]) => {
    document.querySelectorAll(seletor).forEach((formulario) => {
        campos.forEach((nome) => {
            const campo = formulario.elements.namedItem(nome);
            if (campo) campo.required = true;
        });
        const botao = formulario.querySelector('button[type="button"]');
        if (botao) botao.type = 'submit';
        formulario.dataset.mensagem = mensagem;
    });
});

document.querySelectorAll('#foto-perfil, #foto-pet, .editar-profissional-upload input[type="file"]')
    .forEach((entrada) => {
        entrada.addEventListener('change', () => {
            const arquivo = entrada.files?.[0];
            if (!arquivo) return;
            const recipiente = entrada.closest('.editar-perfil-foto, .cadastro-pet-foto, .editar-profissional-foto-linha');
            if (!recipiente) return;
            let imagem = recipiente.querySelector('img:not(.editar-profissional-previa-foto img)');
            if (!imagem) {
                imagem = document.createElement('img');
                imagem.alt = 'Prévia da imagem selecionada';
                if (recipiente.classList.contains('editar-profissional-foto-linha')) {
                    imagem.className = 'editar-profissional-avatar editar-profissional-avatar-foto';
                    recipiente.prepend(imagem);
                    recipiente.querySelector('.editar-profissional-avatar:not(img)')?.remove();
                } else {
                    recipiente.prepend(imagem);
                }
            }
            if (imagem.dataset.previewUrl) URL.revokeObjectURL(imagem.dataset.previewUrl);
            imagem.dataset.previewUrl = URL.createObjectURL(arquivo);
            imagem.src = imagem.dataset.previewUrl;
        });
    });

const tiposImagemGaleria = ['image/jpeg', 'image/png', 'image/webp'];
const tamanhoMaximoGaleria = 5 * 1024 * 1024;

function validarFotosGaleria(arquivos, limite) {
    if (arquivos.length > limite) return `Selecione no máximo ${limite} foto(s).`;
    if (arquivos.some((arquivo) => !tiposImagemGaleria.includes(arquivo.type))) {
        return 'Use somente imagens JPG, PNG ou WebP.';
    }
    if (arquivos.some((arquivo) => arquivo.size > tamanhoMaximoGaleria)) {
        return 'Cada imagem deve ter no máximo 5 MB.';
    }
    return '';
}

document.querySelectorAll('.galeria-upload-form').forEach((formulario) => {
    const secao = formulario.closest('.galeria-perfil');
    const entrada = formulario.querySelector('.galeria-arquivos');
    const modal = formulario.querySelector('.galeria-confirmacao');
    const previas = formulario.querySelector('.galeria-previas');
    const confirmar = formulario.querySelector('.galeria-confirmar');
    const cancelar = formulario.querySelector('.galeria-cancelar');

    const limparSelecao = () => {
        entrada.value = '';
        previas.replaceChildren();
        confirmar.disabled = true;
    };

    secao.querySelectorAll('.galeria-abrir-seletor').forEach((gatilho) => {
        gatilho.addEventListener('click', () => entrada.click());
    });

    entrada.addEventListener('change', () => {
        const arquivos = Array.from(entrada.files || []);
        const erro = validarFotosGaleria(arquivos, Number(entrada.dataset.restantes || 12));
        previas.replaceChildren();
        secao.querySelector('.galeria-erro')?.remove();
        if (erro) {
            mostrarAviso(secao, erro, 'galeria-erro');
            limparSelecao();
            return;
        }
        if (!arquivos.length) return;

        arquivos.forEach((arquivo) => {
            const imagem = document.createElement('img');
            imagem.alt = `Prévia de ${arquivo.name}`;
            imagem.src = URL.createObjectURL(arquivo);
            imagem.addEventListener('load', () => URL.revokeObjectURL(imagem.src), {once: true});
            previas.append(imagem);
        });
        confirmar.disabled = false;
        modal.showModal();
    });

    cancelar.addEventListener('click', () => {
        limparSelecao();
        modal.close();
    });
    modal.addEventListener('cancel', limparSelecao);
});

document.querySelectorAll('.galeria-troca-arquivo').forEach((entrada) => {
    entrada.addEventListener('change', () => {
        const formulario = entrada.closest('.galeria-trocar-form');
        const arquivo = entrada.files?.[0];
        const erro = validarFotosGaleria(arquivo ? [arquivo] : [], 1);
        if (erro) {
            window.alert(erro);
            entrada.value = '';
            formulario.querySelector('button').disabled = true;
            return;
        }
        if (!arquivo) return;
        const imagem = formulario.closest('.galeria-item-gerenciavel').querySelector('img');
        const url = URL.createObjectURL(arquivo);
        imagem.src = url;
        imagem.addEventListener('load', () => URL.revokeObjectURL(url), {once: true});
        formulario.querySelector('button').disabled = false;
    });
});

document.querySelectorAll('form[data-confirmacao]').forEach((formulario) => {
    formulario.addEventListener('submit', (evento) => {
        if (!window.confirm(formulario.dataset.confirmacao)) evento.preventDefault();
    });
});

document.addEventListener('submit', (evento) => {
    const formulario = evento.target;
    if (formulario.matches('form[action][method="post"]')) return;
    if (!formulario.matches('form[data-mensagem]')) return;
    evento.preventDefault();
    if (!formulario.reportValidity()) return;
    mostrarAviso(formulario, formulario.dataset.mensagem);
});

document.querySelectorAll('.chat-escrever').forEach((campoMensagem) => {
    const entrada = campoMensagem.querySelector('input');
    const botao = campoMensagem.querySelector('button');
    const enviar = () => {
        const texto = entrada.value.trim();
        if (!texto) {
            entrada.focus();
            return;
        }
        if (campoMensagem.matches('form[action][method="post"]')) {
            campoMensagem.requestSubmit();
            return;
        }
        const historico = campoMensagem.previousElementSibling;
        const balao = document.createElement('article');
        balao.className = 'chat-balao chat-balao-enviado';
        const mensagem = document.createElement('p');
        const horario = document.createElement('small');
        mensagem.textContent = texto;
        horario.textContent = 'Agora · Enviada';
        balao.append(mensagem, horario);
        historico.append(balao);
        historico.scrollTop = historico.scrollHeight;
        entrada.value = '';
        mostrarAviso(campoMensagem, 'Mensagem adicionada à conversa demonstrativa.');
    };
    botao.addEventListener('click', enviar);
    entrada.addEventListener('keydown', (evento) => {
        if (evento.key === 'Enter') {
            evento.preventDefault();
            enviar();
        }
    });
});

document.querySelectorAll('.diario-filtro').forEach((botao) => {
    botao.addEventListener('click', () => {
        const filtro = botao.textContent.trim();
        document.querySelectorAll('.diario-filtro').forEach((item) => {
            const ativo = item === botao;
            item.classList.toggle('diario-filtro-ativo', ativo);
            item.setAttribute('aria-pressed', ativo ? 'true' : 'false');
        });
        document.querySelectorAll('.diario-registro').forEach((registro) => {
            registro.hidden = filtro !== 'Todos' && registro.dataset.categoria !== filtro;
        });
    });
});

document.querySelectorAll('.booking-servico').forEach((botao) => {
    botao.addEventListener('click', () => {
        document.querySelectorAll('.booking-servico').forEach((item) => {
            const selecionado = item === botao;
            item.classList.toggle('booking-servico-selecionado', selecionado);
            item.setAttribute('aria-pressed', selecionado ? 'true' : 'false');
            item.querySelector('.booking-servico-valor small').textContent =
                selecionado ? 'Selecionado' : 'Ver serviços';
        });
        const nome = botao.querySelector('.booking-servico-texto strong').textContent;
        const campoServico = document.querySelector('#form-servico-booking [name="servico"]');
        if (campoServico) campoServico.value = nome;
        const descricao = botao.querySelector('.booking-servico-texto small').textContent;
        const detalhe = botao.querySelectorAll('.booking-servico-texto small')[1].textContent;
        const preco = botao.querySelector('.booking-servico-valor strong').textContent;
        document.querySelector('.booking-resumo-servico > strong').textContent = nome;
        document.querySelector('.booking-resumo-servico > p').textContent = descricao;
        document.querySelector('.booking-resumo-servico > div span').textContent =
            `${nome} · ${detalhe}`;
        document.querySelector('.booking-resumo-servico > div strong').textContent = preco;
        document.querySelector('.booking-total b').textContent = preco;
    });
});

const seletorPet = document.querySelector('.booking-filtros select[name="pet_id"]');
seletorPet?.addEventListener('change', () => {
    const campoPet = document.querySelector('#form-servico-booking [name="pet_id"]');
    if (campoPet) campoPet.value = seletorPet.value;
});

let horarioBookingSelecionado =
    document.querySelector('.booking-horarios-grade .horario-escolhido')?.dataset.horario || '09:00';

function atualizarResumoBooking() {
    const botaoData = document.querySelector('.booking-calendario-dia.booking-dia-selecionado');
    const dataISO = botaoData?.dataset.data || '';
    const data = dataISO ? new Date(`${dataISO}T12:00:00`) : new Date();
    const diaBookingSelecionado = data.getDate();
    const diaExtenso = new Intl.DateTimeFormat('pt-BR', {weekday: 'long'}).format(data);
    const nomeDia = diaExtenso.charAt(0).toUpperCase() + diaExtenso.slice(1);
    const mesExtenso = new Intl.DateTimeFormat('pt-BR', {month: 'long'}).format(data);
    const dataPorExtenso = `${nomeDia}, ${diaBookingSelecionado} de ${mesExtenso} de ${data.getFullYear()}`;
    const linhaData = document.querySelector('.booking-data-linha span');
    if (linhaData) {
        const detalheHorario = document.createElement('small');
        detalheHorario.textContent =
            `${horarioBookingSelecionado}–${horarioBookingSelecionado.slice(0, 2)}:45`;
        linhaData.replaceChildren(
            document.createTextNode(dataPorExtenso),
            document.createElement('br'), detalheHorario);
    }
    const campoData = document.querySelector('.booking-data-form [name="data"]');
    const campoHorario = document.querySelector('.booking-data-form [name="horario"]');
    if (campoData) campoData.value = dataISO;
    if (campoHorario) campoHorario.value = horarioBookingSelecionado;
    const textoSelecionado = document.querySelector('.booking-data-selecionada');
    if (textoSelecionado) textoSelecionado.textContent = `Selecionado: ${dataPorExtenso}`;
    const textoDia = document.querySelector('.booking-horarios > p');
    if (textoDia) textoDia.textContent = `${nomeDia}, ${String(diaBookingSelecionado).padStart(2, '0')}/${String(data.getMonth() + 1).padStart(2, '0')}`;
    const destaqueHorario = document.querySelector('.booking-horario-destaque strong');
    if (destaqueHorario) destaqueHorario.textContent = `${horarioBookingSelecionado}–${horarioBookingSelecionado.slice(0, 2)}:45`;
    const campoBuscaData = document.querySelector('.booking-filtros input[name="data"]');
    if (campoBuscaData) campoBuscaData.value = `${String(data.getDate()).padStart(2, '0')} ${new Intl.DateTimeFormat('pt-BR', {month: 'short'}).format(data)} · ${horarioBookingSelecionado}`;
}

document.querySelectorAll('.booking-horarios-grade button').forEach((botao) => {
    botao.addEventListener('click', () => {
        document.querySelectorAll('.booking-horarios-grade button').forEach((item) => {
            const selecionado = item === botao;
            item.classList.toggle('horario-escolhido', selecionado);
            item.setAttribute('aria-pressed', selecionado ? 'true' : 'false');
        });
        const horario = botao.textContent.trim();
        horarioBookingSelecionado = horario;
        document.querySelector('.booking-horario-destaque strong').textContent =
            `◷  ${horario}–${horario.slice(0, 2)}:45`;
        atualizarResumoBooking();
    });
});

document.querySelectorAll('.booking-calendario-grade button.booking-calendario-dia')
    .forEach((botao) => {
        botao.addEventListener('click', () => {
            document.querySelectorAll('.booking-calendario-grade button.booking-calendario-dia')
                .forEach((item) => {
                    const selecionado = item === botao;
                    item.classList.toggle('booking-dia-selecionado', selecionado);
                    item.setAttribute('aria-pressed', selecionado ? 'true' : 'false');
                });
            const data = new Date(`${botao.dataset.data}T12:00:00`);
            document.querySelectorAll('.booking-calendario-dia.booking-dia-selecionado')
                .forEach((item) => item.classList.remove('booking-dia-selecionado'));
            botao.classList.add('booking-dia-selecionado');
            const diaExtenso = new Intl.DateTimeFormat('pt-BR', {weekday: 'long'}).format(data);
            const diaNome = diaExtenso.charAt(0).toUpperCase() + diaExtenso.slice(1);
            const mesExtenso = new Intl.DateTimeFormat('pt-BR', {month: 'long'}).format(data);
            const textoData = `${diaNome}, ${botao.dataset.dia} de ${mesExtenso}`;
            document.querySelector('.booking-data-selecionada').textContent =
                `Selecionado: ${textoData}`;
            document.querySelector('.booking-horarios > p').textContent =
                `${diaNome}, ${String(botao.dataset.dia).padStart(2, '0')}/10`;
            atualizarResumoBooking();
        });
    });

document.querySelectorAll('.pagamento-metodo input').forEach((entrada) => {
    entrada.addEventListener('change', () => {
        document.querySelectorAll('.pagamento-metodo').forEach((metodo) => {
            metodo.classList.toggle(
                'pagamento-metodo-selecionado', metodo.querySelector('input').checked);
        });
        const textoPix = document.querySelector('.pagamento-pix-info p');
        if (textoPix) {
            textoPix.textContent = entrada.value === 'pix' ?
                'O código Pix será exibido após a confirmação. Você poderá pagar pelo aplicativo do seu banco.' :
                'Cartão selecionado. O pagamento será apenas demonstrativo e nenhuma cobrança será feita.';
        }
    });
});

const botaoPagamento = document.querySelector('.pagamento-resumo .botao');
if (botaoPagamento) {
    botaoPagamento.addEventListener('click', () => {
        mostrarAviso(
            botaoPagamento.parentElement, 'Pagamento simulado. Nenhuma cobrança foi feita.');
    });
}

const galeriaFotos = document.querySelector('.galeria-fotos');
if (galeriaFotos) {
    const controles = document.querySelectorAll('.perfil-galeria .galeria-controles button');
    controles[0]?.addEventListener(
        'click', () => galeriaFotos.prepend(galeriaFotos.lastElementChild));
    controles[1]?.addEventListener(
        'click', () => galeriaFotos.append(galeriaFotos.firstElementChild));
}

document.querySelectorAll('.agendamentos-dias .agendamento-dia').forEach((dia) => {
    dia.addEventListener('click', () => {
        document.querySelectorAll('.agendamentos-dias .agendamento-dia').forEach((item) => {
            const selecionado = item === dia;
            item.classList.toggle('agendamento-dia-selecionado', selecionado);
            item.setAttribute('aria-pressed', selecionado ? 'true' : 'false');
            item.removeAttribute('aria-current');
            if (selecionado) item.setAttribute('aria-current', 'date');
        });
        const nome = dia.querySelector('span').textContent;
        const data = dia.querySelector('strong').textContent;
        document.querySelector('.agendamentos-lista-topo p').textContent =
            `${nome}, ${data} de outubro`;
    });
    dia.addEventListener('keydown', (evento) => {
        if (evento.key === 'Enter' || evento.key === ' ') {
            evento.preventDefault();
            dia.click();
        }
    });
});
