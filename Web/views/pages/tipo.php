<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-cadastro-perfil">
    <div class="cadastro-perfil-container">
        <header class="cadastro-perfil-cabecalho">
            <p class="cadastro-perfil-etiqueta">CRIE SUA CONTA</p>
            <h1>Como você quer fazer parte?</h1>
            <p>Escolha seu perfil para encontrar o cuidado ideal ou oferecer seu carinho a quem precisa.</p>
        </header>

        <div class="cadastro-perfil-opcoes">
            <a class="cadastro-perfil-opcao cadastro-perfil-tutor" href="tutor.php">
                <div class="cadastro-perfil-arte" aria-hidden="true">
                    <img class="cadastro-perfil-pet" src="assets/img/composicao-companhia.webp" alt="Cão e gato juntos">
                </div>
                <div class="cadastro-perfil-texto">
                    <span class="cadastro-perfil-icone"><?= iconeSvg('heart') ?></span>
                    <p class="cadastro-perfil-etiqueta">PARA QUEM AMA PETS</p>
                    <h2>Sou tutor</h2>
                    <p>Quero encontrar profissionais de confiança e cuidar da rotina do meu pet.</p>
                    <span class="cadastro-perfil-link">Criar conta de tutor <span aria-hidden="true">→</span></span>
                </div>
            </a>

            <a class="cadastro-perfil-opcao cadastro-perfil-profissional" href="profissional.php">
                <div class="cadastro-perfil-arte" aria-hidden="true">
                    <img class="cadastro-perfil-pet" src="assets/img/composicao-alegria.webp" alt="Cachorro alegre">
                </div>
                <div class="cadastro-perfil-texto">
                    <span class="cadastro-perfil-icone"><?= iconeSvg('sparkle') ?></span>
                    <p class="cadastro-perfil-etiqueta">PARA QUEM CUIDA</p>
                    <h2>Sou profissional</h2>
                    <p>Quero compartilhar minha experiência e oferecer serviços para pets.</p>
                    <span class="cadastro-perfil-link">Criar conta profissional <span aria-hidden="true">→</span></span>
                </div>
            </a>
        </div>

        <p class="cadastro-perfil-entrar">Já tem uma conta? <a href="login.php">Entrar</a></p>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
