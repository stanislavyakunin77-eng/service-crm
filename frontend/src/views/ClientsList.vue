<template>
  <div class="clients-list">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1><i class="bi bi-people"></i> Клиенты</h1>
      <button class="btn btn-primary" @click="openAddModal">
        <i class="bi bi-plus-circle"></i> Добавить клиента
      </button>
    </div>

    <div class="card">
      <div class="card-body">
        <div v-if="loading" class="text-center">
          <div class="spinner-border" role="status">
            <span class="visually-hidden">Загрузка...</span>
          </div>
        </div>

        <div v-else-if="clients.length" class="table-responsive">
          <table class="table table-hover">
            <thead>
              <tr>
                <th>ID</th>
                <th>Имя</th>
                <th>Email</th>
                <th>Телефон</th>
                <th>Компания</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="client in clients" :key="client.id">
                <td>{{ client.id }}</td>
                <td>{{ client.firstName }} {{ client.lastName }}</td>
                <td>{{ client.email }}</td>
                <td>{{ client.phoneNumber }}</td>
                <td>{{ client.companyName || '-' }}</td>
                <td>
                  <button class="btn btn-sm btn-warning me-2" @click="editClient(client)">
                    <i class="bi bi-pencil"></i>
                  </button>
                  <button class="btn btn-sm btn-danger" @click="deleteClient(client.id)">
                    <i class="bi bi-trash"></i>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-else class="alert alert-info">
          Нет клиентов. <a href="#" @click.prevent="openAddModal">Добавить первого клиента</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import apiService from '../api/service'

export default {
  name: 'ClientsList',
  data() {
    return {
      clients: [],
      loading: false
    }
  },
  mounted() {
    this.loadClients()
  },
  methods: {
    async loadClients() {
      this.loading = true
      try {
        const response = await apiService.getClients()
        this.clients = response.data.content || []
      } catch (error) {
        console.error('Error loading clients:', error)
        alert('Ошибка при загрузке клиентов')
      } finally {
        this.loading = false
      }
    },
    openAddModal() {
      alert('Функция добавления клиента будет реализована в следующей версии')
    },
    editClient(client) {
      alert(`Редактирование клиента ${client.firstName} ${client.lastName}`)
    },
    async deleteClient(id) {
      if (confirm('Вы уверены?')) {
        try {
          await apiService.deleteClient(id)
          this.loadClients()
        } catch (error) {
          console.error('Error deleting client:', error)
          alert('Ошибка при удалении клиента')
        }
      }
    }
  }
}
</script>

<style scoped>
.clients-list {
  padding: 20px;
}

.bi {
  margin-right: 0.5rem;
}
</style>
