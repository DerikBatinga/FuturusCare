<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-contato">
    <div class="contato-container">
        <section class="contato-hero">
            <div><p class="contato-etiqueta">CONTE COM A GENTE</p><h1>Estamos aqui<br>para ouvir você.</h1><p>Tire suas dúvidas, compartilhe uma sugestão ou peça ajuda com a plataforma.</p></div>
            <div class="contato-hero-arte"><img class="contato-pet" src="assets/img/composicao-acolhimento.webp" alt="Cachorro acolhido pela comunidade FuturusCare"></div>
        </section>

        <div class="contato-conteudo">
            <section class="contato-form-card" aria-labelledby="titulo-mensagem-contato">
                <h2 id="titulo-mensagem-contato">Envie sua mensagem</h2>
                <p>Preencha os campos abaixo. Nossa equipe vai ajudar você.</p>
                <form class="contato-formulario" method="post" action="contato.php">
                <input type="hidden" name="acao" value="contato">
                <?= csrfCampo() ?>
                    <div class="contato-campos-duplos">
                        <label>Nome<input type="text" name="nome" placeholder="Seu nome completo" autocomplete="name"></label>
                        <label>E-mail<input type="email" name="email" placeholder="voce@exemplo.com" autocomplete="email"></label>
                    </div>
                    <label>Assunto<select name="assunto"><option value="">Selecione o assunto</option><option>Dúvida sobre serviços</option><option>Ajuda com agendamento</option><option>Meu perfil</option><option>Outro assunto</option></select></label>
                    <label>Mensagem<textarea name="mensagem" rows="5" placeholder="Como podemos ajudar?"></textarea><small>Não inclua informações pessoais ou bancárias na mensagem.</small></label>
                    <button class="botao botao-primario" type="submit">Enviar mensagem</button>
                </form>
            </section>

            <aside class="contato-lateral">
                <section class="contato-ajuda"><?= iconeSvg('chat') ?><h2>Estamos aqui para ajudar.</h2><p>Prefere conversar? É só chamar.</p><a href="mailto:contato@futuruscare.com.br">contato@futuruscare.com.br</a></section>
                <section class="contato-faq" aria-labelledby="titulo-perguntas-contato"><h2 id="titulo-perguntas-contato">Dúvidas frequentes</h2>
                    <?php foreach ($perguntas as $pergunta): ?>
                        <article><h3><?= htmlspecialchars($pergunta['pergunta'], ENT_QUOTES, 'UTF-8') ?></h3><p><?= htmlspecialchars($pergunta['resposta'], ENT_QUOTES, 'UTF-8') ?></p></article>
                    <?php endforeach; ?>
                </section>
            </aside>
        </div>

        <section class="contato-cta"><div><h2>Seu pet, cercado de cuidado.</h2><p>Conheça as opções que combinam com vocês.</p></div><a class="botao botao-primario" href="servicos.php">Conhecer serviços</a></section>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
