<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-login">
    <div class="login-container">
        <section class="login-apresentacao">
            <div class="login-texto">
                <p class="login-etiqueta">QUE BOM TER VOCÊ DE VOLTA</p>
                <h1>O cuidado continua por aqui.</h1>
                <p>Acesse sua conta para acompanhar os cuidados, conversar com profissionais e organizar a rotina do seu pet.</p>
                <ul>
                    <li><span aria-hidden="true">✓</span> Agendamentos em um só lugar</li>
                    <li><span aria-hidden="true">✓</span> Conversas e atualizações do diário</li>
                </ul>
            </div>
            <div class="login-arte" aria-hidden="true">
                <img class="login-pet" src="assets/img/composicao-companhia.webp" alt="Cão e gato juntos">
            </div>
        </section>

        <section class="login-form-card" aria-labelledby="titulo-login">
            <div class="login-form-intro">
                <p class="login-etiqueta">SUA CONTA FUTURUSCARE</p>
                <h2 id="titulo-login">Vamos começar?</h2>
                <p>Entre com seus dados para continuar.</p>
            </div>
            <form class="login-formulario" method="post" action="login.php">
                <input type="hidden" name="acao" value="login">
                <?= csrfCampo() ?>
                <label class="campo">E-mail
                    <input type="email" name="email" placeholder="voce@exemplo.com" autocomplete="email">
                </label>
                <label class="campo">Senha
                    <input type="password" name="senha" placeholder="Digite sua senha" autocomplete="current-password">
                </label>
                <div class="login-opcoes">
                    <label class="opcao-checkbox"><input type="checkbox" name="lembrar"> Manter conectado</label>
                    <a href="senha.php">Esqueci minha senha</a>
                </div>
                <button class="botao botao-primario botao-cheio" type="submit">Entrar na minha conta</button>
            </form>
            <p class="login-cadastro">Ainda não tem uma conta? <a href="tipo.php">Criar conta</a></p>
            <p class="login-seguranca"><?= iconeSvg('shield') ?> Seus dados são tratados com cuidado e segurança.</p>
        </section>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
