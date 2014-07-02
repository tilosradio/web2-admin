<?php
/**
 * Created by IntelliJ IDEA.
 * User: elek
 * Date: 1/5/14
 * Time: 11:14 AM
 */

namespace Radio\Mapper;


abstract class FieldConverter implements Mapper
{

    private $fieldFrom;

    private $fieldTo;

    function __construct($from, $to = null)
    {
        $this->fieldFrom = $from;
        if (!$to){
            $this->fieldTo = $from;
        } else {
            $this->fieldTo = $to;
        }
    }

    public function map(&$from, &$to, $setter)
    {
        if (array_key_exists($this->fieldFrom, $from)) {
            $var = $this->convert($from[$this->fieldFrom]);
            $setter->set($to, $this->fieldTo, $var);
        }
    }

    abstract protected function convert($from);

    /**
     * @param mixed $fieldFrom
     */
    public function setFieldFrom($fieldFrom)
    {
        $this->fieldFrom = $fieldFrom;
    }

    /**
     * @param null $fieldTo
     */
    public function setFieldTo($fieldTo)
    {
        $this->fieldTo = $fieldTo;
    }


} 