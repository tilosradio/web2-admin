<?php

namespace Radio\Controller;


use Doctrine\ORM\Query\ResultSetMapping;
use Radio\Mapper\ArrayFieldSetter;
use Radio\Mapper\ChildCollection;
use Radio\Mapper\ChildObject;
use Radio\Mapper\DateField;
use Radio\Mapper\EnumField;
use Radio\Mapper\Field;
use Radio\Mapper\ListMapper;
use Radio\Mapper\ObjectMapper;
use Radio\Mapper\ResourceField;
use Swagger\Annotations\Resource;
use Zend\Mvc\Controller\AbstractRestfulController;
use Zend\View\Model\JsonModel;
use Radio\Provider\EntityManager;
use Radio\Mapper\MapperFactory;

class Mix extends BaseController
{

    use EntityManager;

    public function createMapper($m)
    {
        $m->addMapper(new Field("id"));
        $m->addMapper(new Field("title"));
        $m->addMapper(new Field("type"));
        $m->addMapper(new Field("author"));
        $m->addMapper(new Field("file"));

        $file = new ResourceField("file", "http://archive.tilos.hu");
        $file->setContext("/sounds/mixek/");
        $file->setFieldTo("fileLink");

        $type = new EnumField("type", "typeText");
        $type->addValue(0, "Beszélgetés");
        $type->addValue(1, "Zene");

        $m->addMapper($file);
        $m->addMapper($type);

        $c = $m->addMapper(new ChildObject("show"));
        $c->addMapper(new Field("id"));
        $c->addMapper(new Field("name"));
        return $m;
    }

    public function getList()
    {
        try {

            $qb = $this->getEntityManager()->createQueryBuilder();
            $qb->select('t', 's')
                ->from('\Radio\Entity\Mix', 't')
                ->leftJoin("t.show", 's')
                ->setMaxResults(100);
            $q = $qb->getQuery();

            $mix = $q->getArrayResult();

            //var_dump($episodes);
            $result = [];
            $m = $this->createMapper(new ListMapper());
            $m->map($mix, $result, new ArrayFieldSetter());
            return new JsonModel($result);
        } catch (Exception $ex) {
            $this->getResponse()->setStatusCode(500);
            return new JsonModel(array("error" => $ex->getMessage()));
        }
    }

    public function get($e)
    {
        try {

            $id = $e->getRouteMatch()->getParam("id");

            if (!$id) {
                return getList();
            }
            $qb = $this->getEntityManager()->createQueryBuilder();
            $qb->select('t','s')
                ->from('\Radio\Entity\Mix', 't')
                ->where("t.id = :id")
                ->leftJoin("t.show", 's')
                ->setMaxResults(100);
            $q = $qb->getQuery();
            $q->setParameter("id", $id);

            $mix = $q->getArrayResult()[0];

            //var_dump($episodes);
            $result = [];
            $m = $this->createMapper(new ObjectMapper());
            $m->map($mix, $result, new ArrayFieldSetter());
            return new JsonModel($result);
        } catch (Exception $ex) {
            $this->getResponse()->setStatusCode(500);
            return new JsonModel(array("error" => $ex->getMessage()));
        }
    }


}
