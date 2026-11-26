<?php
declare(strict_types=1);

function dados(string $entidade): array
{
    return $_SESSION['fc'][$entidade] ?? [];
}

function usuarioAtual(): ?array
{
    $id = $_SESSION['usuario_id'] ?? null;
    foreach (dados('usuarios') as $usuario) {
        if ((int) $usuario['id'] === (int) $id) {
            return $usuario;
        }
    }
    return null;
}

function perfilProfissional(int $usuarioId): ?array
{
    foreach (dados('profissionais') as $perfil) {
        if ((int) $perfil['usuario_id'] === $usuarioId) {
            return $perfil;
        }
    }
    return null;
}

function proximoId(string $entidade): int
{
    $ids = array_map('intval', array_column(dados($entidade), 'id'));
    return $ids ? max($ids) + 1 : 1;
}

function usuariosDoTutor(int $tutorId): array
{
    return array_values(array_filter(dados('pets'), static fn(array $pet): bool => (int) $pet['tutor_id'] === $tutorId));
}

function agendamentosDoUsuario(?array $usuario = null): array
{
    $usuario ??= usuarioAtual();
    if (!$usuario) {
        return [];
    }

    return array_values(array_filter(dados('agendamentos'), static function (array $agendamento) use ($usuario): bool {
        return $usuario['tipo'] === 'tutor'
            ? (int) $agendamento['tutor_id'] === (int) $usuario['id']
            : (int) $agendamento['profissional_id'] === (int) $usuario['id'];
    }));
}

function buscarAgendamento(int $id, ?array $usuario = null): ?array
{
    foreach (agendamentosDoUsuario($usuario) as $agendamento) {
        if ((int) $agendamento['id'] === $id) {
            return $agendamento;
        }
    }
    return null;
}

function salvarAgendamento(array $novo): void
{
    foreach ($_SESSION['fc']['agendamentos'] as $indice => $agendamento) {
        if ((int) $agendamento['id'] === (int) ($novo['id'] ?? 0)) {
            $_SESSION['fc']['agendamentos'][$indice] = array_merge($agendamento, $novo);
            return;
        }
    }
    $_SESSION['fc']['agendamentos'][] = $novo;
}

function obterPet(int $id, int $tutorId): ?array
{
    foreach (usuariosDoTutor($tutorId) as $pet) {
        if ((int) $pet['id'] === $id) {
            return $pet;
        }
    }
    return null;
}

function profissionaisDisponiveis(): array
{
    $usuarios = dados('usuarios');
    $resultado = [];
    foreach (dados('profissionais') as $perfil) {
        $usuario = null;
        foreach ($usuarios as $candidato) {
            if ((int) $candidato['id'] === (int) $perfil['usuario_id']) {
                $usuario = $candidato;
                break;
            }
        }
        if (!$usuario) {
            continue;
        }
        $resultado[] = array_merge($perfil, [
            'id' => (int) $usuario['id'],
            'nome' => $usuario['nome'],
            'email' => $usuario['email'],
            'telefone' => $usuario['telefone'] ?? '',
            'foto' => $usuario['foto'] ?? '',
        ]);
    }
    return $resultado;
}

function buscarProfissionaisPorServico(string $termo = ''): array
{
    $termo = trim($termo);
    if ($termo === '') {
        return profissionaisDisponiveis();
    }

    return array_values(array_filter(profissionaisDisponiveis(), static function (array $profissional) use ($termo): bool {
        foreach ($profissional['servicos'] ?? [] as $servico) {
            $encontrou = function_exists('mb_stripos')
                ? mb_stripos((string) $servico, $termo, 0, 'UTF-8') !== false
                : stripos((string) $servico, $termo) !== false;
            if ($encontrou) {
                return true;
            }
        }
        return false;
    }));
}

function valorServico(array $profissional, string $servico): float
{
    if (isset($profissional['precos'][$servico])) {
        return (float) $profissional['precos'][$servico];
    }
    return (float) ($profissional['preco'] ?? 45);
}

function galeriaDoUsuario(array $usuario): array
{
    if (($usuario['tipo'] ?? '') === 'profissional') {
        return array_values(perfilProfissional((int) $usuario['id'])['galeria'] ?? []);
    }
    return array_values($usuario['galeria'] ?? []);
}

function definirGaleriaDoUsuario(array $usuario, array $galeria): void
{
    if (($usuario['tipo'] ?? '') === 'profissional') {
        foreach ($_SESSION['fc']['profissionais'] as &$perfil) {
            if ((int) $perfil['usuario_id'] === (int) $usuario['id']) {
                $perfil['galeria'] = array_values($galeria);
                break;
            }
        }
        unset($perfil);
        return;
    }

    foreach ($_SESSION['fc']['usuarios'] as &$item) {
        if ((int) $item['id'] === (int) $usuario['id']) {
            $item['galeria'] = array_values($galeria);
            break;
        }
    }
    unset($item);
}

function temParticipacaoNoPet(int $petId, int $usuarioId): bool
{
    foreach (dados('agendamentos') as $agendamento) {
        if ((int) $agendamento['pet_id'] !== $petId) {
            continue;
        }
        if ((int) $agendamento['tutor_id'] === $usuarioId || (int) $agendamento['profissional_id'] === $usuarioId) {
            return true;
        }
    }
    return false;
}

function horarioOcupado(int $profissionalId, string $data, string $horario): bool
{
    foreach (dados('agendamentos') as $agendamento) {
        if ((int) $agendamento['profissional_id'] === $profissionalId
            && $agendamento['data'] === $data
            && $agendamento['horario'] === $horario
            && !in_array($agendamento['status'] ?? '', ['Cancelado', 'Recusado'], true)) {
            return true;
        }
    }
    return false;
}
