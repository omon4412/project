<template>
  <div class="error-page">
    <div class="error-container">
      <h1>404</h1>
      <p>Страница не найдена</p>
      <router-link to="/">На главную</router-link>
    </div>
  </div>
</template>

<script>
import checkSession from "@/pages/test";

export default {
  async mounted() {
    await checkSession();
  },
}
</script>

<style scoped>
.error-page {
  display: flex;
  align-items: center;
  justify-content: center;
  height: calc(100vh - 57px);
  background-color: #f8f9fa;
}

.error-container {
  text-align: center;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0px 0px 30px 0px #666;
  background-color: #34495e;
}

h1 {
  font-size: 120px;
  margin: 0;
  color: #7c7b7a;
}

p {
  font-size: 18px;
  color: #d7dee3;
}

router-link {
  display: block;
  margin-top: 20px;
  font-size: 16px;
  color: #acbfd2;
  text-decoration: none;
  transition: color 0.3s;
}

router-link:hover {
  color: #0056b3;
}
</style>