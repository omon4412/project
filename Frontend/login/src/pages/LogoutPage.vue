<template>
  <div>
    Logout processing
  </div>
</template>

<script>
import axios from "axios";
import router from "@/router/router";

export default {
  methods: {
    async logout() {
      try {
        await axios.post('http://localhost:5100/api/v1/logout', {}, {
          withCredentials: true
        });
        await router.push('/login');
      } catch (e) {
        this.$refs.notificationRef.showNotification(e.message, 3000, "error");
      }
    }
  },
  mounted() {
    this.logout();
  }
}
</script>

<style scoped>

</style>