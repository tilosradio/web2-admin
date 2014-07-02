<?php
return array(
    'mix-get' => array(
        'type' => 'Radio\Util\CustomSegmentRouter',
        'options' => array(
            'method' => 'GET',
            'route' => '/api/v0/mix',
            'permission' => 'guest',
            'defaults' => array(
                'controller' => 'Radio\Controller\Mix',
                'action' => 'getList'
            ),
        )
    ),
    'mix-list' => array(
        'type' => 'Radio\Util\CustomSegmentRouter',
        'options' => array(
            'method' => 'GET',
            'route' => '/api/v0/mix/:id',
            'permission' => 'guest',
            'defaults' => array(
                'controller' => 'Radio\Controller\Mix',
                'action' => 'get'
            ),
        )
    ),
    
);
