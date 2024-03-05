<template>
  <div class="page">
    <div class="error-container">
      <h1>Main</h1>
    </div>
  </div>
</template>

<script>
import router from "@/router/router";

import checkSession from "@/pages/check";

export default {
  async mounted() {
    const session = await checkSession();
    console.log(session + " mainpage " + "mounted")
    if (session === '') {
      await router.push('/login');
    }
  },
}
</script>

<style scoped>
.page {
  display: flex;
  align-items: center;
  justify-content: center;
  height: calc(100vh - 113px);
}
</style>