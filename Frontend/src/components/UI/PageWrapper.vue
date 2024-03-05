<template>
  <div class="page_wrapper">
    <div v-for="pageNum in totalPages" :key="pageNum" class="page" :class="{ current_page: page === pageNum - 1 }"
         @click="changePage(pageNum)">
      {{ pageNum }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'page-wrapper',
  methods: {
    changePage(pageNumber) {
      this.$emit('update:page', pageNumber - 1);
    }
  },
  props: {
    totalPages: {
      type: Number,
      default: 0,
      required: true
    },
    page: {
      type: Number,
      required: true
    }
  }
}
</script>

<style scoped>
.page_wrapper {
  display: flex;
  margin-top: 15px;
  justify-content: center;
}

.page {
  border: 1px solid black;
  padding: 10px;
  cursor: pointer;
}

.current_page {
  border: 2px solid red;
}
</style>
