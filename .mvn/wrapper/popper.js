require.config({
baseUrl: 'lib',
paths: {
    main: 'main',
    jquery: 'jquery/dist/jquery',
    'jquery-slim': 'jquery/dist/jquery-3.3.1.slim.min',
    'jqueryUnobtrusive': 'jquery-validation-unobtrusive/jquery.validate.unobtrusive',
    'bootstrap': 'bootstrap/dist/js/bootstrap',
    'knockout': 'knockout/knockout-latest',
    'ajax': 'jquery-ajax/jquery.unobtrusive-ajax',
    'shop': '/scripts/shop/shop',
    'domready': 'requirejs/domready',
    'shoporder': '/scripts/shoporder/shoporder',
    'popper': 'popper.js/dist/umd/popper'
}});

define("popper.js", ['popper'], () => {
require(['popper'], () => {
    require(['bootstrap']);
});

define(['main', 'jquery', 'jquery-slim'], () => {
 require(['jqueryUnobtrusive', 'ajax']);
 require(['knockout']);
 require(['jquery']);
 require(['popper.js']);
});