<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-login pagina-recuperar-senha">
    <div class="login-container">
        <section class="login-apresentacao">
            <div class="login-texto">
                <p class="login-etiqueta">ESTAMOS AQUI PARA AJUDAR</p>
                <h1>Vamos encontrar o caminho de volta.</h1>
                <p>É fácil retomar o acesso à sua conta e continuar acompanhando o cuidado do seu pet.</p>
            </div>
            <div class="login-arte" aria-hidden="true">
                <img class="login-forma-laranja" src="assets/img/forma-laranja.svg" alt="">
                <img class="login-forma-lilas" src="assets/img/forma-lavanda.svg" alt="">
                <img class="login-pet" src="assets/img/composicao-curiosidade.webp" alt="Gato curioso">
            </div>
        </section>

        <section class="login-form-card recuperar-form-card" aria-labelledby="titulo-recuperar">
            <a class="recuperar-voltar" href="login.php">← Voltar para entrar</a>
            <div class="login-form-intro">
                <p class="login-etiqueta">RECUPERAÇÃO DE ACESSO</p>
                <h2 id="titulo-recuperar">Esqueceu sua senha?</h2>
                <p>Informe o e-mail usado na sua conta. Vamos mostrar uma confirmação nesta tela.</p>
            </div>
            <form class="login-formulario recuperar-formulario" method="post" action="senha.php">
                <input type="hidden" name="acao" value="recuperar_senha">
                <?= csrfCampo() ?>
                <label class="campo">E-mail
                    <input type="email" name="email" placeholder="voce@exemplo.com" autocomplete="email">
                </label>
                <button class="botao botao-primario botao-cheio" type="submit">Continuar</button>
            </form>
            <p class="recuperar-ajuda">Precisa de ajuda? <a href="contato.php">Fale com a gente</a></p>
        </section>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
