<template>
  <div class="orders-list">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1><i class="bi bi-list-check"></i> Заказы</h1>
      <button class="btn btn-primary" @click="openAddModal">
        <i class="bi bi-plus-circle"></i> Новый заказ
      </button>
    </div>

    <div class="row mb-4">
      <div class="col-md-3">
        <input 
          v-model="filterStatus" 
          type="text" 
          class="form-control" 
          placeholder="Фильтр по статусу"
          @change="loadOrders"
        >
      </div>
    </div>

    <div class="card">
      <div class="card-body">
        <div v-if="loading" class="text-center">
          <div class="spinner-border" role="status">
            <span class="visually-hidden">Загрузка...</span>
          </div>
        </div>

        <div v-else-if="orders.length" class="table-responsive">
          <table class="table table-hover">
            <thead>
              <tr>
                <th>№ Заказа</th>
                <th>Клиент</th>
                <th>Устройство</th>
                <th>Статус</th>
                <th>Смета</th>
                <th>Дата создания</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in orders" :key="order.id">
                <td>{{ order.orderNumber }}</td>
                <td>{{ order.clientName }}</td>
                <td>{{ order.deviceType }} ({{ order.deviceModel }})</td>
                <td>
                  <span :class="`badge bg-${getStatusColor(order.status)}`">
                    {{ getStatusText(order.status) }}
                  </span>
                </td>
                <td>{{ order.estimatedCost ? `${order.estimatedCost} ₽` : '-' }}</td>
                <td>{{ formatDate(order.createdAt) }}</td>
                <td>
                  <router-link :to="`/orders/${order.id}`" class="btn btn-sm btn-info me-2">
                    <i class="bi bi-eye"></i>
                  </router-link>
                  <button class="btn btn-sm btn-danger" @click="deleteOrder(order.id)">
                    <i class="bi bi-trash"></i>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-else class="alert alert-info">
          Нет заказов. <a href="#" @click.prevent="openAddModal">Создать новый заказ</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import apiService from '../api/service'

export default {
  name: 'OrdersList',
  data() {
    return {
      orders: [],
      loading: false,
      filterStatus: ''
    }
  },
  mounted() {
    this.loadOrders()
  },
  methods: {
    async loadOrders() {
      this.loading = true
      try {
        const response = await apiService.getOrders()
        this.orders = response.data.content || []
      } catch (error) {
        console.error('Error loading orders:', error)
        alert('Ошибка при загрузке заказов')
      } finally {
        this.loading = false
      }
    },
    openAddModal() {
      alert('Функция создания заказа будет реализована в следующей версии')
    },
    async deleteOrder(id) {
      if (confirm('Вы уверены?')) {
        try {
          await apiService.deleteOrder(id)
          this.loadOrders()
        } catch (error) {
          console.error('Error deleting order:', error)
          alert('Ошибка при удалении заказа')
        }
      }
    },
    getStatusText(status) {
      const statusMap = {
        'CREATED': 'Создана',
        'RECEIVED': 'Принята',
        'DIAGNOSTICS': 'Диагностика',
        'AWAITING_APPROVAL': 'Ожидание',
        'APPROVED': 'Одобрена',
        'REJECTED': 'Отклонена',
        'IN_PROGRESS': 'В работе',
        'COMPLETED': 'Завершена',
        'PAID': 'Оплачена',
        'RETURNED': 'Выдана'
      }
      return statusMap[status] || status
    },
    getStatusColor(status) {
      const colorMap = {
        'CREATED': 'secondary',
        'RECEIVED': 'info',
        'DIAGNOSTICS': 'warning',
        'AWAITING_APPROVAL': 'warning',
        'APPROVED': 'info',
        'REJECTED': 'danger',
        'IN_PROGRESS': 'primary',
        'COMPLETED': 'success',
        'PAID': 'success',
        'RETURNED': 'success'
      }
      return colorMap[status] || 'secondary'
    },
    formatDate(date) {
      return new Date(date).toLocaleDateString('ru-RU')
    }
  }
}
</script>

<style scoped>
.orders-list {
  padding: 20px;
}

.bi {
  margin-right: 0.5rem;
}
</style>
