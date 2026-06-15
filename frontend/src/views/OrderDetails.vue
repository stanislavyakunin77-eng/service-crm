<template>
  <div class="order-details">
    <div v-if="loading" class="text-center">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Загрузка...</span>
      </div>
    </div>

    <div v-else-if="order">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Заказ {{ order.orderNumber }}</h1>
        <router-link to="/orders" class="btn btn-secondary">
          <i class="bi bi-arrow-left"></i> Назад
        </router-link>
      </div>

      <div class="row">
        <div class="col-md-6">
          <div class="card mb-3">
            <div class="card-header bg-dark text-white">
              <h5 class="mb-0">Основная информация</h5>
            </div>
            <div class="card-body">
              <div class="mb-3">
                <label class="form-label"><strong>Статус:</strong></label>
                <span :class="`badge bg-${getStatusColor(order.status)}`">
                  {{ getStatusText(order.status) }}
                </span>
              </div>
              <div class="mb-3">
                <label class="form-label"><strong>Клиент:</strong></label>
                <p>{{ order.clientName }}</p>
              </div>
              <div class="mb-3">
                <label class="form-label"><strong>Устройство:</strong></label>
                <p>{{ order.deviceType }} - {{ order.deviceModel }} ({{ order.deviceSerial }})</p>
              </div>
              <div class="mb-3">
                <label class="form-label"><strong>Описание:</strong></label>
                <p>{{ order.description }}</p>
              </div>
            </div>
          </div>
        </div>

        <div class="col-md-6">
          <div class="card mb-3">
            <div class="card-header bg-dark text-white">
              <h5 class="mb-0">Исполнители</h5>
            </div>
            <div class="card-body">
              <div class="mb-3">
                <label class="form-label"><strong>Менеджер:</strong></label>
                <p>{{ order.managerName || 'Не назначен' }}</p>
              </div>
              <div class="mb-3">
                <label class="form-label"><strong>Мастер:</strong></label>
                <p>{{ order.masterName || 'Не назначен' }}</p>
              </div>
              <div class="mb-3">
                <label class="form-label"><strong>Смета:</strong></label>
                <p>{{ order.estimatedCost ? `${order.estimatedCost} ₽` : 'Не указана' }}</p>
              </div>
              <div class="mb-3">
                <label class="form-label"><strong>Стоимость:</strong></label>
                <p>{{ order.finalCost ? `${order.finalCost} ₽` : 'Не указана' }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-md-12">
          <div class="card">
            <div class="card-header bg-dark text-white">
              <h5 class="mb-0">Диагностика и примечания</h5>
            </div>
            <div class="card-body">
              <div class="mb-3">
                <label class="form-label"><strong>Диагностика:</strong></label>
                <p>{{ order.diagnostics || 'Не проведена' }}</p>
              </div>
              <div class="mb-3">
                <label class="form-label"><strong>Примечания по ремонту:</strong></label>
                <p>{{ order.repairNotes || 'Отсутствуют' }}</p>
              </div>
              <div class="mb-3">
                <label class="form-label"><strong>Дата создания:</strong></label>
                <p>{{ formatDate(order.createdAt) }}</p>
              </div>
              <div class="mb-3" v-if="order.completedAt">
                <label class="form-label"><strong>Дата завершения:</strong></label>
                <p>{{ formatDate(order.completedAt) }}</p>
              </div>
              <div class="mb-3">
                <label class="form-label"><strong>Оплачено:</strong></label>
                <span :class="`badge bg-${order.isPaid ? 'success' : 'danger'}`">
                  {{ order.isPaid ? 'Да' : 'Нет' }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="row mt-4">
        <div class="col-md-12">
          <button v-if="order.status === 'CREATED'" class="btn btn-primary me-2" @click="receiveOrder">
            <i class="bi bi-check-circle"></i> Принять в сервис
          </button>
          <button v-if="order.status === 'RECEIVED'" class="btn btn-warning me-2" @click="startDiagnostics">
            <i class="bi bi-tools"></i> Начать диагностику
          </button>
          <button v-if="order.status === 'DIAGNOSTICS'" class="btn btn-info me-2" @click="approveDiagnostics">
            <i class="bi bi-check"></i> Одобрить смету
          </button>
          <button v-if="order.status === 'APPROVED'" class="btn btn-success me-2" @click="startRepair">
            <i class="bi bi-hammer"></i> Начать ремонт
          </button>
          <button v-if="order.status === 'IN_PROGRESS'" class="btn btn-success me-2" @click="completeRepair">
            <i class="bi bi-check-lg"></i> Завершить ремонт
          </button>
          <button v-if="order.status === 'COMPLETED'" class="btn btn-info me-2" @click="markAsPaid">
            <i class="bi bi-credit-card"></i> Отметить оплачено
          </button>
          <button class="btn btn-secondary" @click="goBack">
            <i class="bi bi-arrow-left"></i> Назад
          </button>
        </div>
      </div>
    </div>

    <div v-else class="alert alert-danger">
      Заказ не найден
    </div>
  </div>
</template>

<script>
import apiService from '../api/service'

export default {
  name: 'OrderDetails',
  data() {
    return {
      order: null,
      loading: false
    }
  },
  mounted() {
    this.loadOrder()
  },
  methods: {
    async loadOrder() {
      this.loading = true
      try {
        const response = await apiService.getOrderById(this.$route.params.id)
        this.order = response.data
      } catch (error) {
        console.error('Error loading order:', error)
        alert('Ошибка при загрузке заказа')
      } finally {
        this.loading = false
      }
    },
    async receiveOrder() {
      try {
        await apiService.updateOrderStatus(this.order.id, 'RECEIVED')
        this.loadOrder()
      } catch (error) {
        console.error('Error:', error)
        alert('Ошибка при обновлении статуса')
      }
    },
    async startDiagnostics() {
      try {
        await apiService.updateOrderStatus(this.order.id, 'DIAGNOSTICS')
        this.loadOrder()
      } catch (error) {
        console.error('Error:', error)
        alert('Ошибка при обновлении статуса')
      }
    },
    async approveDiagnostics() {
      try {
        await apiService.updateOrderStatus(this.order.id, 'APPROVED')
        this.loadOrder()
      } catch (error) {
        console.error('Error:', error)
        alert('Ошибка при обновлении статуса')
      }
    },
    async startRepair() {
      try {
        await apiService.updateOrderStatus(this.order.id, 'IN_PROGRESS')
        this.loadOrder()
      } catch (error) {
        console.error('Error:', error)
        alert('Ошибка при обновлении статуса')
      }
    },
    async completeRepair() {
      try {
        await apiService.updateOrderStatus(this.order.id, 'COMPLETED')
        this.loadOrder()
      } catch (error) {
        console.error('Error:', error)
        alert('Ошибка при обновлении статуса')
      }
    },
    async markAsPaid() {
      try {
        await apiService.markAsPaid(this.order.id)
        this.loadOrder()
      } catch (error) {
        console.error('Error:', error)
        alert('Ошибка при обновлении статуса')
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
    },
    goBack() {
      this.$router.push('/orders')
    }
  }
}
</script>

<style scoped>
.order-details {
  padding: 20px;
}

.bi {
  margin-right: 0.5rem;
}

.card {
  box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
}
</style>
