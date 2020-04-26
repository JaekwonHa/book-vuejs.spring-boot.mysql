Vue.directive('focus', {
    // 바인딩된 요소가 DOM으로 삽입될때
    inserted: function (el) {
        el.focus();
    }
});
