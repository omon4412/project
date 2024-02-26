<template>
  <div v-if="isVisible" :class="['notification', this.status]">
    <span>{{ message }}</span>
    <button @click="closeNotification" class="close-button">&times;</button>
  </div>
</template>

<script>
export default {
  name: "notification-window",
  data() {
    return {
      isVisible: false,
      message: "",
      status: ""
    };
  },
  methods: {
    showNotification(message, duration = 3000, status = "") {
      this.message = message;
      this.isVisible = true;
      this.setNotificationColor(status);
      setTimeout(() => {
        this.closeNotification();
      }, duration);
    },
    closeNotification() {
      this.isVisible = false;
    },
    setNotificationColor(status) {
      switch (status) {
        case "ok":
          this.status = "ok";
          break;
        case "error":
          this.status = "error";
          break;
        case "warn":
          this.status = "warn";
          break;
        default:
          this.status = "";
      }
    },
  },
};
</script>

<style scoped>
.notification {
  position: fixed;
  right: 0;
  width: 40%;
  background-color: #000000;
  color: white;
  padding: 15px;
  text-align: center;
  box-shadow: 0px -5px 15px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  margin-top: 10px;
  margin-right: 10px;
  border-radius: 10px;
}

.close-button {
  background: none;
  border: none;
  color: white;
  font-size: 18px;
  cursor: pointer;
  position: absolute;
  top: 47%;
  right: 10px;
  transform: translateY(-50%);
}

.error {
  background-color: #c93131;
}

.ok {
  background-color: #89d25b;
}

.warn {
  background-color: #daa142;
}
</style>