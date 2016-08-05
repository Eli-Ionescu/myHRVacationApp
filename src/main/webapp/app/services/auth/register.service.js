(function () {
    'use strict';

    angular
        .module('myHrAppConcediuApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
