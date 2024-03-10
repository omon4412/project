<template>
  <div class="dialog" v-if="show" @click.stop="hideDialog">
    <div @click.stop class="dialog_content">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'custom-dialog',
  props: {
    show: {
      type: Boolean,
      default: false
    },
    route: {
      type: String,
      default: ''
    }
  },
  methods: {
    hideDialog() {
      if (this.route !== '') {
        //history.back();
      }
      this.$emit('update:show', false)
    },
    handleKeyPress(event) {
      if (event.key === 'Escape') {
        this.hideDialog();
      }
    }
  },
  mounted() {
    window.addEventListener('keydown', this.handleKeyPress);
  },
  beforeUnmount() {
    window.removeEventListener('keydown', this.handleKeyPress);
  },
  watch: {
    show() {
      if (this.show) {
        history.pushState(null, null, this.route);
      } else {
        history.back();
      }
    }
  }
}
</script>

<style scoped>
.dialog {
  top: 0px;
  bottom: 0px;
  right: 0px;
  left: 0px;
  background: rgba(0, 0, 0, 0.5);
  position: fixed;
  display: flex;
}

.dialog_content {
  margin: auto;
  background: white;
  border-radius: 12px;
  min-width: 300px;
  min-height: 50px;
  padding: 15px;
}
</style>