<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-mensagens">
    <div class="mensagens-container">
        <section class="mensagens-cabecalho" aria-labelledby="titulo-mensagens">
            <div><h1 id="titulo-mensagens">Mensagens</h1><p>O cuidado também acontece na conversa.</p></div>
            <div class="mensagens-cabecalho-pet" aria-hidden="true">
                <img class="mensagens-forma-laranja" src="assets/img/forma-laranja.svg" alt="">
                <img class="mensagens-forma-lilas" src="assets/img/forma-lavanda.svg" alt="">
                <img class="mensagens-pet" src="assets/img/composicao-em-casa.webp" alt="Gatinho em casa">
            </div>
        </section>

        <div class="mensagens-layout">
            <aside class="mensagens-lista" aria-label="Conversas">
                <div class="mensagens-lista-topo"><h2>Conversas</h2><span><?= $mensagensConversa ? '1' : '0' ?></span></div>
                <label class="mensagens-busca"><span class="sr-only">Buscar conversa</span><input type="search" placeholder="Buscar conversa..."></label>
                <a class="mensagem-contato mensagem-contato-selecionada" href="#conversa-ana" aria-current="page">
                    <span class="mensagem-avatar"><?= escapar(substr($contatoChat['nome'] ?? 'FC', 0, 2)) ?></span><span><strong><?= escapar($contatoChat['nome'] ?? 'Equipe FuturusCare') ?></strong><small><?= escapar($mensagensConversa ? end($mensagensConversa)['data'] : 'Nova conversa') ?></small><small class="mensagem-previa"><?= escapar($mensagensConversa ? end($mensagensConversa)['texto'] : 'Envie a primeira mensagem.') ?></small></span><span class="mensagem-nao-lida" aria-label="Conversa selecionada"></span>
                </a>
                <a class="mensagem-contato" href="#conversa-equipe">
                    <span class="mensagem-avatar mensagem-avatar-equipe">FC</span><span><strong>Equipe FuturusCare</strong><small>Ontem</small><small class="mensagem-previa">Estamos aqui quando precisar.</small></span>
                </a>
                <div class="mensagens-dica">
                    <?= iconeSvg('heart') ?><strong>Uma conversa faz a diferença</strong><p>Combine os detalhes do passeio e compartilhe o que seu pet precisa.</p>
                </div>
            </aside>

            <section class="chat-conversa" id="conversa-ana" aria-labelledby="chat-nome-ana">
                <header class="chat-cabecalho">
                    <span class="mensagem-avatar"><?= escapar(substr($contatoChat['nome'] ?? 'FC', 0, 2)) ?></span><div><h2 id="chat-nome-ana"><?= escapar($contatoChat['nome'] ?? 'Equipe FuturusCare') ?></h2><p>Profissional · Atendimento de <?= escapar($petChat['nome'] ?? 'seu pet') ?></p></div><span class="chat-status"><i></i>Disponível</span>
                </header>
                <div class="chat-mensagens" aria-label="Histórico da conversa">
                    <?php foreach ($mensagensConversa as $mensagem): ?>
                        <article class="chat-balao <?= (int) $mensagem['de'] === (int) $usuarioChat['id'] ? 'chat-balao-enviado' : 'chat-balao-recebido' ?>"><p><?= escapar($mensagem['texto']) ?></p><small><?= escapar($mensagem['data']) ?></small></article>
                    <?php endforeach; ?>
                    <?php if (!$mensagensConversa): ?><p class="chat-data">Comece a conversa por aqui.</p><?php endif; ?>
                    <div class="chat-diario"><span aria-hidden="true">▧</span><div><strong>Diário de <?= escapar($petChat['nome'] ?? 'seu pet') ?></strong><small>Fotos e registros do atendimento</small></div><a href="diario.php">Ver diário</a></div>
                </div>
                <form class="chat-escrever" method="post" action="mensagens.php" aria-label="Escrever mensagem"><input type="hidden" name="acao" value="mensagem">
                <?= csrfCampo() ?>
                    <label class="sr-only" for="mensagem-chat">Escreva sua mensagem</label>
                    <input id="mensagem-chat" name="mensagem" type="text" placeholder="Escreva sua mensagem...">
                    <button class="botao botao-primario" type="button">Enviar</button>
                </form>
            </section>

            <aside class="mensagens-contexto" aria-label="Informações do pet e próximo atendimento">
                <section class="chat-pet-cartao">
                    <div class="chat-pet-imagem">
                        <img class="chat-pet-forma-laranja" src="assets/img/forma-laranja.svg" alt="">
                        <img class="chat-pet-forma-lilas" src="assets/img/forma-lavanda.svg" alt="">
                        <img class="chat-pet-foto" src="<?= escapar($petChat ? imagemPet($petChat) : 'assets/img/composicao-em-casa.webp') ?>" alt="<?= escapar($petChat['nome'] ?? 'Pet') ?>">
                    </div>
                    <a class="chat-link-pequeno" href="tutorpainel.php">Seu companheiro</a>
                    <h2><?= escapar($petChat['nome'] ?? 'Pet') ?></h2><p><?= escapar($petChat['raca'] ?? '') ?> · <?= (int) ($petChat['idade'] ?? 0) ?> anos</p>
                    <h3>Sobre <?= escapar($petChat['nome'] ?? 'o pet') ?></h3><p><?= escapar($petChat['observacoes'] ?? '') ?></p>
                </section>
                <section class="chat-proximo-cartao">
                    <p class="chat-proximo-titulo"><?= iconeSvg('calendar') ?> Próximo cuidado</p><span class="chat-tag-confirmado">Confirmado</span>
                    <h2>Dog Walking</h2><p>Amanhã · sex, 02/10/2026</p><p>09:00 · 45 min · Ana Martins</p>
                    <strong>R$ 45,00</strong>
                    <a class="botao botao-secundario" href="agenda.php">Ver agendamento</a>
                    <a class="botao botao-primario" href="diario.php">Acompanhar diário</a>
                </section>
                <a class="chat-ajuda" href="contato.php">Precisa de ajuda?</a>
            </aside>
        </div>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
