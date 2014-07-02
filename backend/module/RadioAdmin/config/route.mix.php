<?php
return array(

    'mix-create' => array(
        'type' => 'Radio\Util\CustomSegmentRouter',
        'options' => array(
            'method' => 'POST',
            'route' => '/api/v0/mix',
            'permission' => 'admin',
            'defaults' => array(
                'controller' => 'RadioAdmin\Controller\Mix',
                'action' => 'create'
            ),
        )
    ),
    'mix-update' => array(
        'type' => 'Radio\Util\CustomSegmentRouter',
        'options' => array(
            'method' => 'PUT',
            'route' => '/api/v0/mix/:id',
            'permission' => 'admin',
            'defaults' => array(
                'controller' => 'RadioAdmin\Controller\Mix',
                'action' => 'update'
            ),
        )
    )
);
