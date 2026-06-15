<template>
  <div class="app">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container-fluid">
        <router-link to="/" class="navbar-brand">
          <i class="bi bi-tools"></i> Service CRM
        </router-link>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <router-link to="/" class="nav-link" active-class="active">
                <i class="bi bi-speedometer2"></i> Панель управления
              </router-link>
            </li>
            <li class="nav-item">
              <router-link to="/clients" class="nav-link" active-class="active">
                <i class="bi bi-people"></i> Клиенты
              </router-link>
            </li>
            <li class="nav-item">
              <router-link to="/orders" class="nav-link" active-class="active">
                <i class="bi bi-list-check"></i> Заказы
              </router-link>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <main class="container-fluid py-4">
      <router-view></router-view>
    </main>

    <footer class="bg-dark text-white text-center py-3 mt-5">
      <p class="mb-0">&copy; 2024 Service CRM System. Все права защищены.</p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style scoped>
.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

main {
  flex: 1;
}

footer {
  margin-top: auto;
}
</style>
