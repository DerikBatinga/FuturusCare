// Array com os diferentes depoimentos para o carrossel
const depoimentos = [
    {
        texto: "A Futurus Care salvou minha viagem! Minha Luna ficou super bem cuidada e eu recebi fotos dela todos os dias. Recomendo de olhos fechados!",
        autor: "- Maria Silva",
        meta: "Tutora da Luna"
    },
    {
        texto: "Minha Mel é idosinha e precisa de medicação diária. Fiquei muito impressionada com o cuidado e atenção que ela recebeu. A comunicação foi perfeita e recebíamos fotos e vídeos durante todo o dia.",
        autor: "- Roberto Fernandes",
        meta: "Tutor da Mel (Poodle)"
    },
    {
        texto: "Primeira vez que deixo meu Simba com pet sitters e foi uma experiência incrível. Ele é muito medroso com estranhos, mas criou um vínculo incrível com a cuidadora e voltou para casa muito feliz.",
        autor: "- Juliana Reis", 
        meta: "Tutora do Simba (Golden Retriever)"
    },
    {
        texto: "Serviço de emergência salvou a vida da minha Bela! No meio da madrugada ela passou mal e a equipe foi super rápida em nos auxiliar e levar ela ao veterinário. Profissionalismo total em um momento de desespero.",
        autor: "- Pedro Santos",
        meta: "Tutor da Bela (Labrador)"
    },
    {
        texto: "Precisei viajar a trabalho por 10 dias e estava preocupada em deixar meus dois gatos. O serviço foi impecável! Recebi fotos e atualizações todos os dias, e eles voltaram muito bem cuidados e carinhosos.",
        autor: "- Carlos Mendez",
        meta: "Tutor do Pecos e Amendoim (Gatos SRD)"
    },
    {
        texto: "O Thor é um cão grande e enérgico, e eu estava com medo de como ele reagiria. Mas os profissionais da FuturusCare demonstraram total conhecimento sobre comportamento canino e ele ficou super calmo e feliz durante os passeios.",
        autor: "- Amanda Costa",
        meta: "Tutora do Thor (Pastor Alemão)"
    }
];

// Variáveis globais para controle do carrossel de depoimentos
let indiceAtual = 0;
const cartaoDepoimento = document.getElementById('cartaoDepoimento');
const textoDepoimento = document.getElementById('textoDepoimento');
const autorDepoimento = document.getElementById('autorDepoimento');
const metaDepoimento = document.getElementById('metaDepoimento');
const botaoAnterior = document.getElementById('botaoAnterior');
const botaoProximo = document.getElementById('botaoProximo');

/**
 * Atualiza o conteúdo do depoimento com animação de fade
 * @param {Object} depoimento - Objeto contendo os dados do depoimento
 */
function atualizarDepoimento(depoimento) {
    // Inicia animação de saída
    cartaoDepoimento.classList.add('desaparecendo');
    
    // Aguarda a animação de saída completar antes de trocar o conteúdo
    setTimeout(() => {
        textoDepoimento.textContent = depoimento.texto;
        autorDepoimento.textContent = depoimento.autor;
        metaDepoimento.textContent = depoimento.meta;
        
        // Remove animação de saída e aplica animação de entrada
        cartaoDepoimento.classList.remove('desaparecendo');
        cartaoDepoimento.classList.add('aparecendo');
        
        // Remove a animação de entrada após completar
        setTimeout(() => {
            cartaoDepoimento.classList.remove('aparecendo');
        }, 100);
    }, 300);
}

/**
 * Atualiza o estado dos botões de navegação
 * Desabilita botões quando necessário
 */
function atualizarBotoes() {
    botaoAnterior.disabled = indiceAtual === 0;
    botaoProximo.disabled = indiceAtual === depoimentos.length - 1;
}

/**
 * Avança para o próximo depoimento
 */
function proximoDepoimento() {
    if (indiceAtual < depoimentos.length - 1) {
        indiceAtual++;
        atualizarDepoimento(depoimentos[indiceAtual]);
        atualizarBotoes();
    }
}

/**
 * Volta para o depoimento anterior
 */
function depoimentoAnterior() {
    if (indiceAtual > 0) {
        indiceAtual--;
        atualizarDepoimento(depoimentos[indiceAtual]);
        atualizarBotoes();
    }
}

/**
 * Animação de contagem progressiva dos números das estatísticas
 */
function animarNumeros() {
    // Configuração das estatísticas a serem animadas
    const estatisticas = [
        { elemento: document.querySelectorAll('.numero-estatistica')[0], alvo: 500, sufixo: '+' },
        { elemento: document.querySelectorAll('.numero-estatistica')[1], alvo: 200, sufixo: '+' },
        { elemento: document.querySelectorAll('.numero-estatistica')[2], alvo: 5, sufixo: '' },
        { elemento: document.querySelectorAll('.numero-estatistica')[3], alvo: 4.9, sufixo: '', decimal: true }
    ];

    // Anima cada estatística individualmente
    estatisticas.forEach(estat => {
        let atual = 0;
        const incremento = estat.decimal ? estat.alvo / 100 : estat.alvo / 50;
        
        // Adiciona classe de animação
        estat.elemento.classList.add('contando');
        
        // Timer para incrementar o número gradualmente
        const temporizador = setInterval(() => {
            atual += incremento;
            
            // Para a animação quando atinge o valor alvo
            if (atual >= estat.alvo) {
                atual = estat.alvo;
                clearInterval(temporizador);
                estat.elemento.classList.remove('contando');
            }
            
            // Atualiza o texto com formatação apropriada
            if (estat.decimal) {
                estat.elemento.textContent = atual.toFixed(1) + estat.sufixo;
            } else {
                estat.elemento.textContent = Math.floor(atual) + estat.sufixo;
            }
        }, 40); // Intervalo de 40ms para animação suave
    });
}

/**
 * Inicialização quando o DOM estiver carregado
 */
document.addEventListener('DOMContentLoaded', function() {
    
    // ==================== MENU MOBILE ====================
    const alternadorMobile = document.querySelector('.alternador-menu-mobile');
    const linksNavegacao = document.querySelector('.links-navegacao');
    
    if (alternadorMobile && linksNavegacao) {
        // Abrir/fechar menu mobile
        alternadorMobile.addEventListener('click', function() {
            linksNavegacao.classList.toggle('mobile-ativo');
            
            // Trocar ícone do menu
            const icone = alternadorMobile.querySelector('i');
            if (linksNavegacao.classList.contains('mobile-ativo')) {
                icone.className = 'fas fa-times';
            } else {
                icone.className = 'fas fa-bars';
            }
            
            // Prevenir scroll quando menu estiver aberto
            document.body.style.overflow = linksNavegacao.classList.contains('mobile-ativo') ? 'hidden' : '';
        });
        
        // Fechar menu ao clicar em um link
        linksNavegacao.addEventListener('click', function(e) {
            if (e.target.tagName === 'A') {
                linksNavegacao.classList.remove('mobile-ativo');
                alternadorMobile.querySelector('i').className = 'fas fa-bars';
                document.body.style.overflow = '';
            }
        });
        
        // Fechar menu ao clicar fora dele
        document.addEventListener('click', function(e) {
            if (!linksNavegacao.contains(e.target) && !alternadorMobile.contains(e.target)) {
                linksNavegacao.classList.remove('mobile-ativo');
                alternadorMobile.querySelector('i').className = 'fas fa-bars';
                document.body.style.overflow = '';
            }
        });
    }
    
    // ===== CONFIGURAÇÃO DO OBSERVER PARA ANIMAÇÃO DAS ESTATÍSTICAS =====
    const secaoEstatisticas = document.querySelector('.secao-estatisticas');
    let foiAnimado = false; // Flag para evitar múltiplas animações

    // Intersection Observer para detectar quando a seção fica visível
    const observador = new IntersectionObserver((entradas) => {
        entradas.forEach(entrada => {
            // Inicia animação quando 50% da seção estiver visível
            if (entrada.isIntersecting && !foiAnimado) {
                animarNumeros();
                foiAnimado = true;
            }
        });
    }, {
        threshold: 0.5 // Ativa quando 50% do elemento estiver visível
    });

    // Inicia a observação da seção de estatísticas
    if (secaoEstatisticas) {
        observador.observe(secaoEstatisticas);
    }

    // ===== CONFIGURAÇÃO DOS EVENT LISTENERS =====
    
    // Botões de navegação dos depoimentos
    if (botaoProximo && botaoAnterior) {
        botaoProximo.addEventListener('click', proximoDepoimento);
        botaoAnterior.addEventListener('click', depoimentoAnterior);
        
        // Inicializa o estado dos botões
        atualizarBotoes();
    }

    // ===== FUNCIONALIDADES EXTRAS =====
    
    /**
     * Smooth scroll para links internos (se houver)
     */
    document.querySelectorAll('a[href^="#"]').forEach(ancora => {
        ancora.addEventListener('click', function (e) {
            e.preventDefault();
            const alvo = document.querySelector(this.getAttribute('href'));
            if (alvo) {
                alvo.scrollIntoView({
                    behavior: 'smooth'
                });
            }
        });
    });

    /**
     * Adiciona efeito de hover aos cards de serviços para dispositivos touch
     */
    const cartoesServico = document.querySelectorAll('.cartao-servico');
    cartoesServico.forEach(cartao => {
        // Para dispositivos touch, adiciona toggle no tap
        cartao.addEventListener('touchstart', function() {
            // Remove active de outros cards
            cartoesServico.forEach(outroCartao => {
                if (outroCartao !== cartao) {
                    outroCartao.classList.remove('toque-ativo');
                }
            });
            // Toggle no card atual
            this.classList.toggle('toque-ativo');
        });
    });

    /**
     * Lazy loading para imagens (se implementado futuramente)
     */
    if ('IntersectionObserver' in window) {
        const observadorImagem = new IntersectionObserver((entradas, observador) => {
            entradas.forEach(entrada => {
                if (entrada.isIntersecting) {
                    const img = entrada.target;
                    img.src = img.dataset.src;
                    img.classList.remove('preguicoso');
                    observadorImagem.unobserve(img);
                }
            });
        });

        // Observa todas as imagens com classe 'preguicoso'
        document.querySelectorAll('img[data-src]').forEach(img => {
            observadorImagem.observe(img);
        });
    }

    /**
     * Analytics tracking para cliques em botões importantes (placeholder)
     */
    document.querySelectorAll('.botao-hero, .botao-negocios').forEach(botao => {
        botao.addEventListener('click', function(e) {
            // Aqui você pode adicionar tracking de analytics
            console.log('Botão clicado:', this.textContent.trim());
            
            // Exemplo de Google Analytics (descomente quando configurar)
            // gtag('event', 'click', {
            //     'event_category': 'CTA',
            //     'event_label': this.textContent.trim()
            // });
        });
    });

    /**
     * Melhor experiência em dispositivos móveis
     */
    const ehMobile = window.innerWidth <= 768;
    if (ehMobile) {
        // Desabilita animações 3D em dispositivos móveis para melhor performance
        document.body.classList.add('dispositivo-mobile');
        
        // Adiciona classe para ajustes específicos de mobile
        cartoesServico.forEach(cartao => {
            cartao.classList.add('cartao-mobile');
        });
    }

    /**
     * Otimização de performance - reduz animações se o usuário preferir
     */
    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
        // Remove animações para usuários que preferem movimento reduzido
        document.body.classList.add('movimento-reduzido');
    }
});

/**
 * Função para redimensionamento da janela
 */
window.addEventListener('resize', function() {
    // Ajusta layout se necessário
    const ehMobile = window.innerWidth <= 768;
    const cartoesServico = document.querySelectorAll('.cartao-servico');
    
    if (ehMobile) {
        document.body.classList.add('dispositivo-mobile');
        cartoesServico.forEach(cartao => {
            cartao.classList.add('cartao-mobile');
        });
    } else {
        document.body.classList.remove('dispositivo-mobile');
        cartoesServico.forEach(cartao => {
            cartao.classList.remove('cartao-mobile', 'toque-ativo');
        });
    }
});

/**
 * Função para melhorar a acessibilidade
 */
document.addEventListener('keydown', function(e) {
    // Navegação por teclado no carrossel de depoimentos
    if (e.key === 'ArrowLeft' && !botaoAnterior.disabled) {
        depoimentoAnterior();
    } else if (e.key === 'ArrowRight' && !botaoProximo.disabled) {
        proximoDepoimento();
    }
});

// ===== EXPORTAÇÕES PARA USO GLOBAL (SE NECESSÁRIO) =====
window.FuturusCare = {
    proximoDepoimento,
    depoimentoAnterior,
    animarNumeros
};