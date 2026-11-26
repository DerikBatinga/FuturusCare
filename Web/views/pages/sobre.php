<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-sobre">
    <div class="sobre-container">
        <header class="sobre-cabecalho"><p class="sobre-etiqueta">NOSSO JEITO DE CUIDAR</p><h1>Cuidado que aproxima.</h1><p>Conectamos tutores e profissionais para uma rotina com mais cuidado, carinho e tranquilidade.</p></header>

        <section class="sobre-hero">
            <div class="sobre-hero-texto"><p class="sobre-etiqueta">FEITO PARA VOCÊS</p><h2>Mais carinho.<br>Mais tranquilidade.<br>Todos os dias.</h2><p>Confiança, atenção aos detalhes e respeito ao bem-estar de cada pet guiam nosso trabalho.</p><a class="botao botao-primario" href="servico.php">Encontrar um cuidador</a></div>
            <div class="sobre-hero-arte"><img src="assets/img/composicao-amizade.webp" alt="Cão e gato juntos" class="sobre-hero-pets"></div>
        </section>

        <section class="sobre-valores" aria-label="O que nos move">
            <article class="sobre-motivo"><div class="sobre-motivo-arte"><img src="assets/img/composicao-acolhimento.webp" alt="Um cão recebendo carinho"></div><div><?= iconeSvg('heart') ?><h2>O que nos move</h2><p>Do primeiro passeio ao registro do cuidado, cada momento merece atenção. Criamos um jeito simples de manter todo mundo por perto.</p></div></article>
            <div class="sobre-pilares"><h2>Cuidar bem começa com...</h2><article class="sobre-pilar sobre-pilar-bem-estar"><?= iconeSvg('heart') ?><div><h3>Bem-estar</h3><p>A rotina, o ritmo e as necessidades de cada pet vêm primeiro.</p></div></article><article class="sobre-pilar sobre-pilar-confianca"><?= iconeSvg('check') ?><div><h3>Confiança</h3><p>Combinados claros e proximidade entre tutores e profissionais.</p></div></article><article class="sobre-pilar sobre-pilar-proximidade"><?= iconeSvg('chat') ?><div><h3>Proximidade</h3><p>Conversas e registros para acompanhar seu companheiro, mesmo de longe.</p></div><div class="sobre-pilar-links"><a href="diario.php">Conhecer nosso diário →</a><a href="contato.php">Falar com a gente →</a></div></article></div>
        </section>

        <section class="sobre-etapas" aria-labelledby="titulo-sobre-etapas"><div class="sobre-secao-titulo"><p class="sobre-etiqueta">SIMPLES PARA VOCÊS</p><h2 id="titulo-sobre-etapas">Do encontro ao cuidado, tudo mais simples.</h2></div><div class="sobre-etapas-grade">
            <?php foreach ($etapasCuidado as $etapa): ?>
                <article class="sobre-etapa"><span><?= htmlspecialchars($etapa['numero'], ENT_QUOTES, 'UTF-8') ?></span><h3><?= htmlspecialchars($etapa['titulo'], ENT_QUOTES, 'UTF-8') ?></h3><p><?= htmlspecialchars($etapa['texto'], ENT_QUOTES, 'UTF-8') ?></p></article>
            <?php endforeach; ?>
        </div></section>

        <section class="sobre-cta"><div><p class="sobre-etiqueta">UM PRÓXIMO PASSO</p><h2>Um próximo cuidado cheio de carinho.</h2><p>Encontre uma opção que combine com vocês.</p></div><a class="botao botao-primario" href="servicos.php">Explorar serviços</a></section>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
