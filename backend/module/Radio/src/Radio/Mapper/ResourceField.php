<?php

namespace Radio\Mapper;

/**
 * Resource field mapping based on name.
 *
 * @package Radio\Mapper
 */
class ResourceField extends FieldConverter
{

    private $baseUrl;

    private $context = "/upload/";

    function __construct($name, $baseUrl)
    {
        parent::__construct($name);
        $this->baseUrl = $baseUrl;
    }


    protected function  convert($from)
    {
        if ($from) {
            return $this->baseUrl . $this->context . $from;
        } else {
            return null;
        }
    }

    /**
     * @param string $context
     */
    public function setContext($context)
    {
        $this->context = $context;
    }


}