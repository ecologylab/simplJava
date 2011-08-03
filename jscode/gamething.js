
function item(json,price,owner_name,name)
{
    this._simpl_class_descriptor='{"class_descriptor":{"name":"ecologylab.translators.javascript.test.Item", "tag_name":"item", "described_class":"ecologylab.translators.javascript.test.Item", "described_class_simple_name":"Item", "described_class_package_name":"ecologylab.translators.javascript.test", "simpl.id":"156265924", "super_class":{"name":"ecologylab.serialization.ElementState", "tag_name":"element", "described_class":"ecologylab.serialization.ElementState", "described_class_simple_name":"ElementState", "described_class_package_name":"ecologylab.serialization"}, "field_descriptor":[{"name":"price", "tag_name":"price", "field":"price", "type":"18", "scalar_type":"float", "xml_hint":"XML_ATTRIBUTE", "field_type":"float", "declaring_class_descriptor":{"simpl.ref":"156265924"}},{"name":"ownerName", "tag_name":"owner_name", "field":"ownerName", "type":"18", "scalar_type":"String", "xml_hint":"XML_ATTRIBUTE", "field_type":"String", "declaring_class_descriptor":{"simpl.ref":"156265924"}},{"name":"name", "tag_name":"name", "field":"name", "type":"18", "scalar_type":"String", "xml_hint":"XML_ATTRIBUTE", "field_type":"String", "declaring_class_descriptor":{"simpl.ref":"156265924"}}]}}'
    this._simpl_object_name = "item";
    this._simpl_collection_types = {};
    this._simpl_map_types = {};
    if(json)
    {
        jsonConstruct(json,this);
        return;
    }
    else
    {
        if(price) this.price = price;
        if(owner_name) this.owner_name = owner_name;
        if(name) this.name = name;
    }
}


function player(json,name,strength,speed,skin)
{
    this._simpl_class_descriptor='{"class_descriptor":{"name":"ecologylab.translators.javascript.test.Player", "tag_name":"player", "described_class":"ecologylab.translators.javascript.test.Player", "described_class_simple_name":"Player", "described_class_package_name":"ecologylab.translators.javascript.test", "simpl.id":"302785728", "super_class":{"name":"ecologylab.serialization.ElementState", "tag_name":"element", "described_class":"ecologylab.serialization.ElementState", "described_class_simple_name":"ElementState", "described_class_package_name":"ecologylab.serialization"}, "field_descriptor":[{"name":"name", "tag_name":"name", "field":"name", "type":"18", "scalar_type":"String", "xml_hint":"XML_ATTRIBUTE", "field_type":"String", "declaring_class_descriptor":{"simpl.ref":"302785728"}},{"name":"strength", "tag_name":"strength", "field":"strength", "type":"18", "scalar_type":"int", "xml_hint":"XML_ATTRIBUTE", "field_type":"int", "declaring_class_descriptor":{"simpl.ref":"302785728"}},{"name":"speed", "tag_name":"speed", "field":"speed", "type":"18", "scalar_type":"int", "xml_hint":"XML_ATTRIBUTE", "field_type":"int", "declaring_class_descriptor":{"simpl.ref":"302785728"}},{"name":"skin", "tag_name":"skin", "field":"skin", "type":"18", "scalar_type":"int", "xml_hint":"XML_ATTRIBUTE", "field_type":"int", "declaring_class_descriptor":{"simpl.ref":"302785728"}}]}}'
    this._simpl_object_name = "player";
    this._simpl_collection_types = {};
    this._simpl_map_types = {};
    if(json)
    {
        jsonConstruct(json,this);
        return;
    }
    else
    {
        if(name) this.name = name;
        if(strength) this.strength = strength;
        if(speed) this.speed = speed;
        if(skin) this.skin = skin;
    }
}


function human(json,name,strength,speed,skin,rank,level,cash)
{
    this._simpl_class_descriptor='{"class_descriptor":{"name":"ecologylab.translators.javascript.test.Human", "tag_name":"human", "described_class":"ecologylab.translators.javascript.test.Human", "described_class_simple_name":"Human", "described_class_package_name":"ecologylab.translators.javascript.test", "simpl.id":"1791844235", "super_class":{"name":"ecologylab.translators.javascript.test.Player", "tag_name":"player", "described_class":"ecologylab.translators.javascript.test.Player", "described_class_simple_name":"Player", "described_class_package_name":"ecologylab.translators.javascript.test", "simpl.id":"302785728", "super_class":{"name":"ecologylab.serialization.ElementState", "tag_name":"element", "described_class":"ecologylab.serialization.ElementState", "described_class_simple_name":"ElementState", "described_class_package_name":"ecologylab.serialization"}, "field_descriptor":[{"name":"name", "tag_name":"name", "field":"name", "type":"18", "scalar_type":"String", "xml_hint":"XML_ATTRIBUTE", "field_type":"String", "declaring_class_descriptor":{"simpl.ref":"302785728"}},{"name":"strength", "tag_name":"strength", "field":"strength", "type":"18", "scalar_type":"int", "xml_hint":"XML_ATTRIBUTE", "field_type":"int", "declaring_class_descriptor":{"simpl.ref":"302785728"}},{"name":"speed", "tag_name":"speed", "field":"speed", "type":"18", "scalar_type":"int", "xml_hint":"XML_ATTRIBUTE", "field_type":"int", "declaring_class_descriptor":{"simpl.ref":"302785728"}},{"name":"skin", "tag_name":"skin", "field":"skin", "type":"18", "scalar_type":"int", "xml_hint":"XML_ATTRIBUTE", "field_type":"int", "declaring_class_descriptor":{"simpl.ref":"302785728"}}]}, "field_descriptor":[{"name":"rank", "tag_name":"rank", "field":"rank", "type":"18", "scalar_type":"int", "xml_hint":"XML_ATTRIBUTE", "field_type":"int", "declaring_class_descriptor":{"simpl.ref":"1791844235"}},{"name":"level", "tag_name":"level", "field":"level", "type":"18", "scalar_type":"int", "xml_hint":"XML_ATTRIBUTE", "field_type":"int", "declaring_class_descriptor":{"simpl.ref":"1791844235"}},{"name":"cash", "tag_name":"cash", "field":"cash", "type":"18", "scalar_type":"float", "xml_hint":"XML_ATTRIBUTE", "field_type":"float", "declaring_class_descriptor":{"simpl.ref":"1791844235"}}]}}'
    this._simpl_object_name = "human";
    this._simpl_collection_types = {};
    this._simpl_map_types = {};
    if(json)
    {
        jsonConstruct(json,this);
        return;
    }
    else
    {
        if(name) this.name = name;
        if(strength) this.strength = strength;
        if(speed) this.speed = speed;
        if(skin) this.skin = skin;
        if(rank) this.rank = rank;
        if(level) this.level = level;
        if(cash) this.cash = cash;
    }
}


function movements(json,time,moves)
{
    this._simpl_class_descriptor='{"class_descriptor":{"name":"ecologylab.translators.javascript.test.Movements", "tag_name":"movements", "described_class":"ecologylab.translators.javascript.test.Movements", "described_class_simple_name":"Movements", "described_class_package_name":"ecologylab.translators.javascript.test", "simpl.id":"1651462312", "super_class":{"name":"ecologylab.serialization.ElementState", "tag_name":"element", "described_class":"ecologylab.serialization.ElementState", "described_class_simple_name":"ElementState", "described_class_package_name":"ecologylab.serialization", "simpl.id":"1228283922"}, "field_descriptor":[{"name":"time", "tag_name":"time", "field":"time", "type":"18", "scalar_type":"float", "xml_hint":"XML_ATTRIBUTE", "field_type":"float", "declaring_class_descriptor":{"simpl.ref":"1651462312"}},{"name":"moves", "tag_name":"moves", "field":"moves", "element_class":"ecologylab.translators.javascript.test.Move", "is_generic":"true", "type":"4", "collection_or_map_tag_name":"moves", "field_type":"ArrayList", "generic_parameters_string":"&lt;Move&gt;", "element_class_descriptor":{"name":"ecologylab.translators.javascript.test.Move", "tag_name":"move", "described_class":"ecologylab.translators.javascript.test.Move", "described_class_simple_name":"Move", "described_class_package_name":"ecologylab.translators.javascript.test", "simpl.id":"755301112", "super_class":{"simpl.ref":"1228283922"}, "field_descriptor":[{"name":"x", "tag_name":"x", "field":"x", "type":"18", "scalar_type":"float", "xml_hint":"XML_ATTRIBUTE", "field_type":"float", "declaring_class_descriptor":{"simpl.ref":"755301112"}},{"name":"y", "tag_name":"y", "field":"y", "type":"18", "scalar_type":"float", "xml_hint":"XML_ATTRIBUTE", "field_type":"float", "declaring_class_descriptor":{"simpl.ref":"755301112"}},{"name":"sneaking", "tag_name":"sneaking", "field":"sneaking", "type":"18", "scalar_type":"boolean", "xml_hint":"XML_ATTRIBUTE", "field_type":"boolean", "declaring_class_descriptor":{"simpl.ref":"755301112"}},{"name":"defending", "tag_name":"defending", "field":"defending", "type":"18", "scalar_type":"boolean", "xml_hint":"XML_ATTRIBUTE", "field_type":"boolean", "declaring_class_descriptor":{"simpl.ref":"755301112"}},{"name":"moveTime", "tag_name":"move_time", "field":"moveTime", "type":"18", "scalar_type":"float", "xml_hint":"XML_ATTRIBUTE", "field_type":"float", "declaring_class_descriptor":{"simpl.ref":"755301112"}}]}, "declaring_class_descriptor":{"simpl.ref":"1651462312"}, "library_namespaces":{"library_namespace":["ecologylab.translators.javascript.test"]}, "collection_type":{"name":"simpl.types.collection.ArrayList", "simple_name":"ArrayList", "java_type_name":"java.util.ArrayList", "c_sharp_type_name":"List", "objective_c_type_name":"NSMutableArray", "package_name":"java.util"}}]}}'
    this._simpl_object_name = "movements";
    this._simpl_collection_types = {"moves":"move"};
    this._simpl_map_types = {};
    if(json)
    {
        jsonConstruct(json,this);
        return;
    }
    else
    {
        if(time) this.time = time;
        if(moves) this.moves = moves;
    }
}


function bank(json,items)
{
    this._simpl_class_descriptor='{"class_descriptor":{"name":"ecologylab.translators.javascript.test.Bank", "tag_name":"bank", "described_class":"ecologylab.translators.javascript.test.Bank", "described_class_simple_name":"Bank", "described_class_package_name":"ecologylab.translators.javascript.test", "simpl.id":"1177264411", "super_class":{"name":"ecologylab.serialization.ElementState", "tag_name":"element", "described_class":"ecologylab.serialization.ElementState", "described_class_simple_name":"ElementState", "described_class_package_name":"ecologylab.serialization", "simpl.id":"1228283922"}, "field_descriptor":[{"name":"itemMap", "tag_name":"item_map", "field":"itemMap", "element_class":"ecologylab.translators.javascript.test.Item", "is_generic":"true", "type":"6", "collection_or_map_tag_name":"items", "field_type":"HashMap", "generic_parameters_string":"&lt;String, Item&gt;", "element_class_descriptor":{"name":"ecologylab.translators.javascript.test.Item", "tag_name":"item", "described_class":"ecologylab.translators.javascript.test.Item", "described_class_simple_name":"Item", "described_class_package_name":"ecologylab.translators.javascript.test", "simpl.id":"156265924", "super_class":{"simpl.ref":"1228283922"}, "field_descriptor":[{"name":"price", "tag_name":"price", "field":"price", "type":"18", "scalar_type":"float", "xml_hint":"XML_ATTRIBUTE", "field_type":"float", "declaring_class_descriptor":{"simpl.ref":"156265924"}},{"name":"ownerName", "tag_name":"owner_name", "field":"ownerName", "type":"18", "scalar_type":"String", "xml_hint":"XML_ATTRIBUTE", "field_type":"String", "declaring_class_descriptor":{"simpl.ref":"156265924"}},{"name":"name", "tag_name":"name", "field":"name", "type":"18", "scalar_type":"String", "xml_hint":"XML_ATTRIBUTE", "field_type":"String", "declaring_class_descriptor":{"simpl.ref":"156265924"}}]}, "declaring_class_descriptor":{"simpl.ref":"1177264411"}, "library_namespaces":{"library_namespace":["ecologylab.translators.javascript.test"]}, "collection_type":{"name":"simpl.types.collection.HashMap", "simple_name":"HashMap", "java_type_name":"java.util.HashMap", "c_sharp_type_name":"Dictionary", "objective_c_type_name":"NSDictionary", "package_name":"java.util", "is_map":"true"}}]}}'
    this._simpl_object_name = "bank";
    this._simpl_collection_types = {};
    this._simpl_map_types = {"items":"item"};
    if(json)
    {
        jsonConstruct(json,this);
        return;
    }
    else
    {
        if(items) this.items = items;
    }
}


function move(json,x,y,sneaking,defending,move_time)
{
    this._simpl_class_descriptor='{"class_descriptor":{"name":"ecologylab.translators.javascript.test.Move", "tag_name":"move", "described_class":"ecologylab.translators.javascript.test.Move", "described_class_simple_name":"Move", "described_class_package_name":"ecologylab.translators.javascript.test", "simpl.id":"755301112", "super_class":{"name":"ecologylab.serialization.ElementState", "tag_name":"element", "described_class":"ecologylab.serialization.ElementState", "described_class_simple_name":"ElementState", "described_class_package_name":"ecologylab.serialization"}, "field_descriptor":[{"name":"x", "tag_name":"x", "field":"x", "type":"18", "scalar_type":"float", "xml_hint":"XML_ATTRIBUTE", "field_type":"float", "declaring_class_descriptor":{"simpl.ref":"755301112"}},{"name":"y", "tag_name":"y", "field":"y", "type":"18", "scalar_type":"float", "xml_hint":"XML_ATTRIBUTE", "field_type":"float", "declaring_class_descriptor":{"simpl.ref":"755301112"}},{"name":"sneaking", "tag_name":"sneaking", "field":"sneaking", "type":"18", "scalar_type":"boolean", "xml_hint":"XML_ATTRIBUTE", "field_type":"boolean", "declaring_class_descriptor":{"simpl.ref":"755301112"}},{"name":"defending", "tag_name":"defending", "field":"defending", "type":"18", "scalar_type":"boolean", "xml_hint":"XML_ATTRIBUTE", "field_type":"boolean", "declaring_class_descriptor":{"simpl.ref":"755301112"}},{"name":"moveTime", "tag_name":"move_time", "field":"moveTime", "type":"18", "scalar_type":"float", "xml_hint":"XML_ATTRIBUTE", "field_type":"float", "declaring_class_descriptor":{"simpl.ref":"755301112"}}]}}'
    this._simpl_object_name = "move";
    this._simpl_collection_types = {};
    this._simpl_map_types = {};
    if(json)
    {
        jsonConstruct(json,this);
        return;
    }
    else
    {
        if(x) this.x = x;
        if(y) this.y = y;
        if(sneaking) this.sneaking = sneaking;
        if(defending) this.defending = defending;
        if(move_time) this.move_time = move_time;
    }
}


function refer_to_self(json,some_data,reference_to_self)
{
    this._simpl_class_descriptor='{"class_descriptor":{"name":"ecologylab.translators.javascript.test.ReferToSelf", "tag_name":"refer_to_self", "described_class":"ecologylab.translators.javascript.test.ReferToSelf", "described_class_simple_name":"ReferToSelf", "described_class_package_name":"ecologylab.translators.javascript.test", "simpl.id":"1250696665", "super_class":{"name":"ecologylab.serialization.ElementState", "tag_name":"element", "described_class":"ecologylab.serialization.ElementState", "described_class_simple_name":"ElementState", "described_class_package_name":"ecologylab.serialization"}, "field_descriptor":[{"name":"someData", "tag_name":"some_data", "field":"someData", "type":"18", "scalar_type":"String", "xml_hint":"XML_ATTRIBUTE", "field_type":"String", "declaring_class_descriptor":{"simpl.ref":"1250696665"}},{"name":"referenceToSelf", "tag_name":"reference_to_self", "field":"referenceToSelf", "element_class":"ecologylab.translators.javascript.test.ReferToSelf", "type":"3", "composite_tag_name":"reference_to_self", "field_type":"ReferToSelf", "element_class_descriptor":{"simpl.ref":"1250696665"}, "declaring_class_descriptor":{"simpl.ref":"1250696665"}, "library_namespaces":{"library_namespace":["ecologylab.translators.javascript.test"]}}]}}'
    this._simpl_object_name = "refer_to_self";
    this._simpl_collection_types = {};
    this._simpl_map_types = {};
    if(json)
    {
        jsonConstruct(json,this);
        return;
    }
    else
    {
        if(some_data) this.some_data = some_data;
        if(reference_to_self) this.reference_to_self = reference_to_self;
    }
}


function computer(json,name,strength,speed,skin,difficulty,type,ai)
{
    this._simpl_class_descriptor='{"class_descriptor":{"name":"ecologylab.translators.javascript.test.Computer", "tag_name":"computer", "described_class":"ecologylab.translators.javascript.test.Computer", "described_class_simple_name":"Computer", "described_class_package_name":"ecologylab.translators.javascript.test", "simpl.id":"1128671313", "super_class":{"name":"ecologylab.translators.javascript.test.Player", "tag_name":"player", "described_class":"ecologylab.translators.javascript.test.Player", "described_class_simple_name":"Player", "described_class_package_name":"ecologylab.translators.javascript.test", "simpl.id":"302785728", "super_class":{"name":"ecologylab.serialization.ElementState", "tag_name":"element", "described_class":"ecologylab.serialization.ElementState", "described_class_simple_name":"ElementState", "described_class_package_name":"ecologylab.serialization"}, "field_descriptor":[{"name":"name", "tag_name":"name", "field":"name", "type":"18", "scalar_type":"String", "xml_hint":"XML_ATTRIBUTE", "field_type":"String", "declaring_class_descriptor":{"simpl.ref":"302785728"}},{"name":"strength", "tag_name":"strength", "field":"strength", "type":"18", "scalar_type":"int", "xml_hint":"XML_ATTRIBUTE", "field_type":"int", "declaring_class_descriptor":{"simpl.ref":"302785728"}},{"name":"speed", "tag_name":"speed", "field":"speed", "type":"18", "scalar_type":"int", "xml_hint":"XML_ATTRIBUTE", "field_type":"int", "declaring_class_descriptor":{"simpl.ref":"302785728"}},{"name":"skin", "tag_name":"skin", "field":"skin", "type":"18", "scalar_type":"int", "xml_hint":"XML_ATTRIBUTE", "field_type":"int", "declaring_class_descriptor":{"simpl.ref":"302785728"}}]}, "field_descriptor":[{"name":"difficulty", "tag_name":"difficulty", "field":"difficulty", "type":"18", "scalar_type":"float", "xml_hint":"XML_ATTRIBUTE", "field_type":"float", "declaring_class_descriptor":{"simpl.ref":"1128671313"}},{"name":"type", "tag_name":"type", "field":"type", "type":"18", "scalar_type":"String", "xml_hint":"XML_ATTRIBUTE", "field_type":"String", "declaring_class_descriptor":{"simpl.ref":"1128671313"}},{"name":"ai", "tag_name":"ai", "field":"ai", "type":"18", "scalar_type":"String", "xml_hint":"XML_ATTRIBUTE", "field_type":"String", "declaring_class_descriptor":{"simpl.ref":"1128671313"}}]}}'
    this._simpl_object_name = "computer";
    this._simpl_collection_types = {};
    this._simpl_map_types = {};
    if(json)
    {
        jsonConstruct(json,this);
        return;
    }
    else
    {
        if(name) this.name = name;
        if(strength) this.strength = strength;
        if(speed) this.speed = speed;
        if(skin) this.skin = skin;
        if(difficulty) this.difficulty = difficulty;
        if(type) this.type = type;
        if(ai) this.ai = ai;
    }
}

