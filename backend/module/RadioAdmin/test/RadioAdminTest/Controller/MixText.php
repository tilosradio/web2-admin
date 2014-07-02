<?php

namespace RadioAdminTest\Controller;


use Doctrine\Common\DataFixtures\Executor\ORMExecutor;
use Doctrine\Common\DataFixtures\Loader;
use Doctrine\Common\DataFixtures\Purger\ORMPurger;
use Radio\Controller\Atom;
use RadioAdmin\Controller\Mix;
use RadioAdmin\Controller\Url;
use RadioAdmin\Controller\User;
use RadioTest\Bootstrap;
use RadioTest\Fixitures\BaseData;
use Zend\Http\Request;
use Zend\Json\Json;
use Zend\Mvc\MvcEvent;
use Zend\Mvc\Router\Http\TreeRouteStack as HttpRouter;
use Zend\Mvc\Router\RouteMatch;


class MixTest extends TestBase
{

    protected function setUp()
    {
        $this->initTest("\RadioAdmin\Controller\Mix", new Mix());
        $this->baseData();
    }

    public function testCreateUrl()
    {

        $this->user = $this->createUser(1, "admin", "admin");
        //given

        $this->routeMatch->setParam('action', 'update');
        $this->request->setMethod("put");
        $this->routeMatch->setParam('id', 2);

        $this->request->getHeaders()->addHeaderLine("content-type: application/json");
        $this->request->setContent(Json::encode(
            ['id' => 2, 'title' => 'asdasd', 'show' => ['id' => 1]]));

        //when
        $result = $this->controller->dispatch($this->request);
        $response = $this->controller->getResponse();
        //then

        $res = $result->getVariables();
        //var_dump($res);

        $this->assertTrue($res['success']);

    }


}

?>