const formatter = new Intl.DateTimeFormat('KST', {
    year: 'numeric', month: 'long', week: 'long', day: 'numeric',
    hour: 'numeric', minute: 'numeric', second: 'numeric'
});
Vue.filter('datetime', function (value) {
    if (!value) return '';
    const date = new Date(value);
    return formatter.format(date);
});
