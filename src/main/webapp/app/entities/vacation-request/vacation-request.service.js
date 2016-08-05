(function() {
    'use strict';
    angular
        .module('myHrAppConcediuApp')
        .factory('VacationRequest', VacationRequest);

    VacationRequest.$inject = ['$resource', 'DateUtils'];

    function VacationRequest ($resource, DateUtils) {
        var resourceUrl =  'api/vacation-requests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.period = DateUtils.convertDateTimeFromServer(data.period);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'approve': {method: 'PUT'},
            'reject': {method: 'PUT'}
        });
    }
})();
