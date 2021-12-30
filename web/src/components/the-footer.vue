<template>
  <a-layout-footer style="text-align: center">
    Spring Boot + Vue3 构建Wiki知识管理系统, 欢迎：{{user.name}}
  </a-layout-footer>
</template>


<script lang="ts">
import {defineComponent, computed} from 'vue';
import store from "@/store";

export default defineComponent({
  name: 'the-footer',
  setup () {
    const user = computed(() => store.state.user)

    return {
      user,
    }
  }
});
</script>