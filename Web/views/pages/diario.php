<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-diario">
    <div class="diario-container">
        <div class="diario-cabecalho">
            <div><p class="diario-etiqueta">ROTINA COMPARTILHADA</p><h1>Diário de <?= escapar($petDiario['nome'] ?? 'pet') ?></h1><p>Acompanhe os pequenos momentos e cuidados do dia.</p></div>
            <div class="diario-cabecalho-acoes"><a href="tutorpainel.php">Meu perfil</a><a class="botao botao-primario" href="registro.php">+ Novo registro</a></div>
        </div>

        <div class="diario-filtros" aria-label="Categorias do diário">
            <button class="diario-filtro diario-filtro-ativo" type="button">Todos</button>
            <button class="diario-filtro" type="button">Alimentação</button>
            <button class="diario-filtro" type="button">Água</button>
            <button class="diario-filtro" type="button">Passeio</button>
            <button class="diario-filtro" type="button">Bem-estar</button>
        </div>

        <div class="diario-layout">
            <section class="diario-registros" aria-label="Registros de hoje">
                <?php foreach ($registros as $registro): ?>
                    <article class="diario-registro" data-categoria="<?= htmlspecialchars($registro['categoria'], ENT_QUOTES, 'UTF-8') ?>">
                        <div class="diario-registro-marca"><span><?= iconeSvg($registro['icone']) ?></span><time><?= $registro['hora'] ?></time></div>
                        <div class="diario-registro-conteudo">
                            <span class="diario-categoria"><?= htmlspecialchars($registro['categoria'], ENT_QUOTES, 'UTF-8') ?></span>
                            <h2><?= htmlspecialchars($registro['titulo'], ENT_QUOTES, 'UTF-8') ?></h2>
                            <p><?= htmlspecialchars($registro['texto'], ENT_QUOTES, 'UTF-8') ?></p>
                            <small><?= htmlspecialchars($registro['autor'], ENT_QUOTES, 'UTF-8') ?></small>
                        </div>
                    </article>
                <?php endforeach; ?>
                <?php if (!$registros): ?><p class="diario-registro-vazio">Ainda não há registros para este pet. Adicione a primeira atualização.</p><?php endif; ?>
            </section>

            <aside class="diario-lateral">
                <section class="diario-conexao">
                    <p class="diario-etiqueta">CUIDADO EM CONJUNTO</p>
                    <h2>Cuidado que aproxima.</h2>
                    <p>Tutor e profissional acompanham as mesmas atualizações, em um só lugar.</p>
                    <dl><div><dt>Pet acompanhado</dt><dd><?= escapar($petDiario['nome'] ?? '—') ?></dd></div><div><dt>Responsável</dt><dd><?= escapar($usuarioDiario['nome']) ?></dd></div><div><dt>Profissional</dt><dd><?= escapar((profissionaisDisponiveis()[0]['nome'] ?? 'A definir')) ?></dd></div></dl>
                    <a href="mensagens.php">Conversar com Ana →</a>
                </section>
                <section class="diario-proximo">
                    <span class="diario-proximo-tag">AMANHÃ</span>
                    <h2>Mais um passeio feliz.</h2>
                    <p>Sexta, 02 de outubro</p>
                    <p>09:00–09:45 · Dog Walking</p>
                    <a href="agenda.php">Ver agendamento →</a>
                </section>
                <figure class="diario-pet-arte">
                    <img class="diario-forma" src="assets/img/forma-laranja.svg" alt="">
                    <img class="diario-forma-lilas" src="assets/img/forma-lavanda.svg" alt="">
                    <img class="diario-pet" src="assets/img/composicao-brincadeira.webp" alt="Cachorro em momento de brincadeira">
                    <figcaption>Cada momento merece cuidado.</figcaption>
                </figure>
            </aside>
        </div>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
