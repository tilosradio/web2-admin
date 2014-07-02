<?php

namespace RadioTest\Controller;

use RadioTest\Bootstrap;
use Zend\Mvc\Router\Http\TreeRouteStack as HttpRouter;
use Application\Controller\IndexController;
use Zend\Http\Request;
use Zend\Http\Response;
use Zend\Mvc\MvcEvent;
use Zend\Mvc\Router\RouteMatch;
use PHPUnit_Framework_TestCase;

class ShowTest extends TestBase
{


    protected function setUp()
    {
        $this->initTest("Radio\Controller\Mix", new \Radio\Controller\Mix());
        $this->baseData();
    }

    public function testShowGet()
    {
        //when        
        $this->routeMatch->setParam('id', '1');
        $this->routeMatch->setParam('action', 'get');


        $result = $this->controller->dispatch($this->request);
        $response = $this->controller->getResponse();
        //then

        $mix = $result->getVariables();
        //var_dump($mix);
        $this->assertEquals($mix['fileLink'], "http://archive.tilos.hu/sounds/mixek/home.mp3");
        $this->assertEquals($mix['typeText'], "Zene");
        $this->assertEquals($mix['show']['id'], 1);

    }

}

?>