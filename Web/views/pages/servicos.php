<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-servicos">
    <section class="servicos-hero" aria-labelledby="titulo-pagina-servicos">
        <div class="servicos-hero-texto">
            <p class="home-sobretitulo">CUIDADO • COMPANHIA • BEM-ESTAR</p>
            <h1 id="titulo-pagina-servicos">Um cuidado para<br>cada momento.</h1>
            <p>Passeios, companhia e bem-estar. Encontre o serviço que combina com a rotina do seu melhor amigo.</p>
            <a class="botao botao-primario" href="#lista-servicos">Encontrar um profissional <span aria-hidden="true">→</span></a>
        </div>
        <div class="servicos-hero-arte" aria-label="Gato recebendo carinho">
            <img class="servicos-forma-laranja" src="assets/img/forma-laranja.svg" alt="">
            <img class="servicos-forma-lavanda" src="assets/img/forma-lavanda.svg" alt="">
            <img class="servicos-gato" src="assets/img/composicao-carinho.webp" alt="Gatinho recebendo carinho">
        </div>
    </section>

    <section class="servicos-catalogo container" id="lista-servicos" aria-label="Todos os serviços">
        <nav class="servicos-filtros" aria-label="Categorias de serviço">
            <a class="filtro-servico filtro-ativo" href="#lista-servicos" aria-current="page">Todos os serviços</a>
            <a class="filtro-servico" href="#dog-walking">Passeios</a>
            <a class="filtro-servico" href="#pet-sitting">Em casa</a>
            <a class="filtro-servico" href="#banho-tosa">Bem-estar</a>
        </nav>
        <div class="servicos-grade">
            <?php foreach ($servicos as $servico): ?>
                <article class="servico-card <?= htmlspecialchars($servico['classe'], ENT_QUOTES, 'UTF-8') ?>" id="<?= htmlspecialchars($servico['id'], ENT_QUOTES, 'UTF-8') ?>">
                    <div class="servico-card-arte">
                        <img class="servico-pet" src="<?= htmlspecialchars($servico['imagem'], ENT_QUOTES, 'UTF-8') ?>" alt="<?= htmlspecialchars($servico['alt'], ENT_QUOTES, 'UTF-8') ?>">
                    </div>
                    <div class="servico-card-conteudo">
                        <div class="servico-card-meta">
                            <span class="servico-icone"><?= iconeSvg($servico['icone']) ?></span>
                            <span class="etiqueta etiqueta-<?= $servico['categoria'] === 'Mais procurado' ? 'pessego' : 'verde' ?>"><?= htmlspecialchars($servico['categoria'], ENT_QUOTES, 'UTF-8') ?></span>
                        </div>
                        <h2><?= htmlspecialchars($servico['nome'], ENT_QUOTES, 'UTF-8') ?></h2>
                        <p class="servico-card-descricao"><?= htmlspecialchars($servico['descricao'], ENT_QUOTES, 'UTF-8') ?></p>
                        <div class="servico-card-detalhe"><?= htmlspecialchars($servico['detalhe'], ENT_QUOTES, 'UTF-8') ?></div>
                        <strong class="servico-card-preco"><?= htmlspecialchars($servico['preco'], ENT_QUOTES, 'UTF-8') ?></strong>
                        <a class="servico-card-link" href="servico.php">Ver profissionais <span aria-hidden="true">→</span></a>
                    </div>
                </article>
            <?php endforeach; ?>
        </div>
    </section>

    <section class="servicos-como container" aria-labelledby="titulo-como-cuidar">
        <h2 id="titulo-como-cuidar">É simples cuidar de quem você ama.</h2>
        <div class="servicos-passos">
            <article><span>01</span><h3>Escolha o cuidado</h3><p>Encontre o serviço que faz sentido para vocês.</p></article>
            <article><span>02</span><h3>Conheça profissionais</h3><p>Veja perfis e encontre alguém de confiança.</p></article>
            <article><span>03</span><h3>Acompanhe de perto</h3><p>Combine os detalhes e fique por dentro da rotina.</p></article>
        </div>
        <div class="servicos-ajuda">
            <div><h3>Precisa de uma mão para escolher?</h3><p>A gente ajuda você a encontrar o cuidado ideal.</p></div>
            <a class="botao botao-contorno" href="mailto:contato@futuruscare.com.br">Fale com a gente <span aria-hidden="true">→</span></a>
        </div>
    </section>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
