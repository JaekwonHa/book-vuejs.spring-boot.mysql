<template>
  <div id="app">
    <router-view/>
  </div>
</template>
<script>
export default {
  name: 'App'
}
</script>

<style lang="scss">
  html {
    font-size: 14px;
  }
  .from-group {
    .error {
      line-height: 1;
      display: none;
      margin-top: 5px;
    }
  }
  .filed-error {
    .error {
      display: block;
      color: #ff0000;
    }
  }
</style>
