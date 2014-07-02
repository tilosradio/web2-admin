<?php

namespace RadioAdmin\Controller;

use Radio\Controller\BaseController;
use Radio\Mapper\ArrayFieldSetter;
use Radio\Mapper\DateField;
use Radio\Mapper\ChildObject;
use Radio\Mapper\Field;
use Radio\Mapper\ListMapper;
use Radio\Mapper\ObjectFieldSetter;
use Radio\Mapper\ObjectMapper;
use Radio\Mapper\TimestampField;
use Zend\Mvc\Controller\AbstractRestfulController;
use Zend\View\Model\JsonModel;
use Radio\Provider\EntityManager;
use Radio\Mapper\MapperFactory;


class Mix extends BaseController
{

    use EntityManager;

    public function update($e)
    {

        $id = $this->params()->fromRoute("id");
        $data = $this->getRawData($e);
        $mix = $this->getEntityManager()->find("\Radio\Entity\Mix", $id);
        $mapper = new ObjectMapper(new ObjectFieldSetter($this->getEntityManager()));
        $mapper->addMapper(new Field("file"));
        $mapper->addMapper(new Field("name"));
        $mapper->addMapper(new Field("title"));
        $mapper->addMapper(new Field("author"));
        $mapper->addMapper(new Field("type"));
        $mapper->addMapper(new ChildObject("show", "\Radio\Entity\Show"));
        $mapper->map($data, $mix);
        $this->getEntityManager()->flush();
        return new JsonModel(array("success" => true));
    }


    public function create($e)
    {

        $data = $this->getRawData($e);

        $mix = new \Radio\Entity\Mix();

        $mapper = new ObjectMapper(new ObjectFieldSetter($this->getEntityManager()));
        $mapper->addMapper(new Field("name"));
        $mapper->addMapper(new Field("file"));
        $mapper->addMapper(new Field("title"));
        $mapper->addMapper(new Field("type"));
        $mapper->addMapper(new TimestampField("date"));
        $mapper->addMapper(new Field("author"));
        $mapper->addMapper(new TimestampField("date"));
        $mapper->map($data, $mix);

        $this->getEntityManager()->persist($mix);
        $this->getEntityManager()->flush();

        return new JsonModel(array("success" => true, "data" => array("id" => $mix->getId())));
    }


}

