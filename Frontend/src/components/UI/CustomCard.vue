<template>
  <div class="card">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: 'custom-card'
}
</script>

<style scoped>

.card {
  position: relative;
  display: flex;
  flex-direction: column;
  min-width: 0;
  word-wrap: break-word;
  background-color: #fff;
  background-clip: border-box;
  border-radius: 5px;
  box-shadow: 0px 0px 3px 0px #666;
}
</style>