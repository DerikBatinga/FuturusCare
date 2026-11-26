<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-mensagens">
    <div class="mensagens-container">
        <section class="mensagens-cabecalho" aria-labelledby="titulo-mensagens-profissional">
            <div><h1 id="titulo-mensagens-profissional">Mensagens</h1><p>Converse com as famílias e combine cada detalhe do cuidado.</p></div>
            <div class="mensagens-cabecalho-pet" aria-hidden="true">
                <img class="mensagens-forma-laranja" src="assets/img/forma-laranja.svg" alt="">
                <img class="mensagens-forma-lilas" src="assets/img/forma-lavanda.svg" alt="">
                <img class="mensagens-pet" src="assets/img/composicao-alegria.webp" alt="Cachorro alegre">
            </div>
        </section>

        <div class="mensagens-layout">
            <aside class="mensagens-lista" aria-label="Conversas com tutores">
                <div class="mensagens-lista-topo"><h2>Conversas</h2><span>3</span></div>
                <label class="mensagens-busca"><span class="sr-only">Buscar conversa</span><input type="search" placeholder="Buscar tutor ou pet..."></label>
                <a class="mensagem-contato mensagem-contato-selecionada" href="#conversa-bruno" aria-current="page">
                    <span class="mensagem-avatar"><?= escapar(substr($contatoChat['nome'] ?? 'FC', 0, 2)) ?></span><span><strong><?= escapar($contatoChat['nome'] ?? 'Tutor') ?></strong><small><?= escapar($mensagensConversa ? end($mensagensConversa)['data'] : 'Nova conversa') ?></small><small class="mensagem-previa"><?= escapar($mensagensConversa ? end($mensagensConversa)['texto'] : 'Envie a primeira mensagem.') ?></small></span><span class="mensagem-nao-lida" aria-label="Conversa selecionada"></span>
                </a>
                <a class="mensagem-contato" href="#conversa-marina">
                    <span class="mensagem-avatar mensagem-avatar-equipe">MC</span><span><strong>Marina Costa</strong><small>Ontem</small><small class="mensagem-previa">Te envio os detalhes ainda hoje.</small></span>
                </a>
                <a class="mensagem-contato" href="#conversa-equipe">
                    <span class="mensagem-avatar mensagem-avatar-equipe">FC</span><span><strong>Equipe FuturusCare</strong><small>Segunda-feira</small><small class="mensagem-previa">Conte com a gente.</small></span>
                </a>
                <div class="mensagens-dica"><?= iconeSvg('heart') ?><strong>Comunicação que aproxima</strong><p>Alinhe as informações importantes antes de cada atendimento.</p></div>
            </aside>

            <section class="chat-conversa" id="conversa-bruno" aria-labelledby="chat-nome-bruno">
                <header class="chat-cabecalho">
                    <span class="mensagem-avatar"><?= escapar(substr($contatoChat['nome'] ?? 'FC', 0, 2)) ?></span><div><h2 id="chat-nome-bruno"><?= escapar($contatoChat['nome'] ?? 'Tutor') ?></h2><p>Tutor de <?= escapar($petChat['nome'] ?? 'um pet') ?></p></div><span class="chat-status"><i></i>Disponível</span>
                </header>
                <div class="chat-mensagens" aria-label="Histórico da conversa">
                    <?php foreach ($mensagensConversa as $mensagem): ?>
                        <article class="chat-balao <?= (int) $mensagem['de'] === (int) $usuarioChat['id'] ? 'chat-balao-enviado' : 'chat-balao-recebido' ?>"><p><?= escapar($mensagem['texto']) ?></p><small><?= escapar($mensagem['data']) ?></small></article>
                    <?php endforeach; ?>
                    <?php if (!$mensagensConversa): ?><p class="chat-data">Comece a conversa por aqui.</p><?php endif; ?>
                    <div class="chat-diario"><span aria-hidden="true">▧</span><div><strong>Diário de <?= escapar($petChat['nome'] ?? 'seu pet') ?></strong><small>Adicione fotos e registros do atendimento</small></div><a href="registro.php">Adicionar registro</a></div>
                </div>
                <form class="chat-escrever" method="post" action="chatprofissional.php" aria-label="Escrever mensagem"><input type="hidden" name="acao" value="mensagem">
                <?= csrfCampo() ?>
                    <label class="sr-only" for="mensagem-profissional">Escreva sua mensagem</label>
                    <input id="mensagem-profissional" name="mensagem" type="text" placeholder="Escreva sua mensagem...">
                    <button class="botao botao-primario" type="button">Enviar</button>
                </form>
            </section>

            <aside class="mensagens-contexto" aria-label="Informações do pet e atendimento">
                <section class="chat-pet-cartao">
                    <div class="chat-pet-imagem">
                        <img class="chat-pet-forma-laranja" src="assets/img/forma-laranja.svg" alt="">
                        <img class="chat-pet-forma-lilas" src="assets/img/forma-lavanda.svg" alt="">
                        <img class="chat-pet-foto" src="<?= escapar($petChat ? imagemPet($petChat) : 'assets/img/composicao-em-casa.webp') ?>" alt="<?= escapar($petChat['nome'] ?? 'Pet atendido') ?>">
                    </div>
                    <span class="chat-link-pequeno">Pet do atendimento</span>
                    <h2><?= escapar($petChat['nome'] ?? 'Pet') ?></h2><p><?= escapar($petChat['raca'] ?? '') ?> · <?= (int) ($petChat['idade'] ?? 0) ?> anos</p>
                    <h3>Cuidados importantes</h3><p>Levar água, manter a guia próxima e fazer pausas durante o passeio.</p>
                    <a class="chat-ajuda" href="tutorpainel.php">Ver informações do pet</a>
                </section>
                <section class="chat-proximo-cartao">
                    <p class="chat-proximo-titulo"><?= iconeSvg('calendar') ?> Atendimento de hoje</p><span class="chat-tag-confirmado">Confirmado</span>
                    <h2>Dog Walking</h2><p>01/10/2026 · 09:00</p><p>45 min · Bruno Almeida</p>
                    <a class="botao botao-secundario" href="agenda.php">Ver agenda</a>
                    <a class="botao botao-primario" href="registro.php">Registrar cuidado</a>
                </section>
            </aside>
        </div>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
