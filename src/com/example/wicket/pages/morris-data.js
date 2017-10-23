$(function() {
    Morris.Area({
        element: 'morris-area-chart',
        data: [{
            period: '2015 Q3',
            Sales: 2666,
            Policies: null,
            Claims: 2647
        }, {
            period: '2015 Q4',
            Sales: 2778,
            Policies: 2294,
            Claims: 2441
        },{
            period: '2016 Q1',
            Sales: 2666,
            Policies: null,
            Claims: 2647
        }, {
            period: '2016 Q2',
            Sales: 2778,
            Policies: 2294,
            Claims: 2441
        }, {
            period: '2016 Q3',
            Sales: 4912,
            Policies: 1969,
            Claims: 2501
        }, {
            period: '2016 Q4',
            Sales: 3767,
            Policies: 3597,
            Claims: 5689
        }, {
            period: '2017 Q1',
            Sales: 6810,
            Policies: 1914,
            Claims: 2293
        }, {
            period: '2017 Q2',
            Sales: 5670,
            Policies: 4293,
            Claims: 1881
        }, {
            period: '2017 Q3',
            Sales: 4820,
            Policies: 3795,
            Claims: 1588
        }, {
            period: '2017 Q4',
            Sales: 15073,
            Policies: 5967,
            Claims: 5175
        }],
        xkey: 'period',
        ykeys: ['Sales', 'Policies', 'Claims'],
        labels: ['Sales', 'Policies', 'Claims'],
        pointSize: 2,
        hideHover: 'auto',
        resize: true
    });
});