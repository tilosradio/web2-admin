<?php

namespace Radio\Mapper;

/**
 * Field string mapper based on fixed values.
 *
 * @package Radio\Mapper
 */
class EnumField extends FieldConverter
{

    private $values;

    private $context = "/upload/";

    function __construct($from, $to)
    {
        parent::__construct($from, $to);
        $this->values = [];
    }

    public function addValue($key, $value)
    {
        $this->values[$key] = $value;
    }

    protected function convert($from)
    {
        return $this->values[$from];
    }



}