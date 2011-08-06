var _simpl_ref_index_map = {};
var _simpl_class_descriptor_map = {};


var _db_almost_never = 3;
var _db_light_warning = 2;
var _db_warning = 1;
var _db_critical_error = 0;

var _db_level = _db_critical_error;//set to a larger number for more reporting

//look for firebug var so that you can use alert or something
function console_message(input, level)
{
   if (console.log) {
     if(!level)
     {
        console.log(input);
     }
     else
     {
        if(level <= _db_level)
           console.log(input);
     }
   }
   else
   {
      alert("You really really need firebug installed to do debugging.");
   }
}

function addOrGetFromClassDescriptorMap(function_name,descriptor_json)
{
   console_message("addOrGetFromClassDescriptorMap",3);
   var returnObject = null;
   returnObject = _simpl_class_descriptor_map[function_name];
   if(returnObject)
      return returnObject;
   if(function_name == "class_descriptor" || function_name == "field_descriptor")
   {
      returnObject = descriptor_json;
   }
   else
   {
      returnObject = simplDeserialize(descriptor_json);
   }
   _simpl_class_descriptor_map[function_name] = returnObject;
   return returnObject;
}

//This function takes a string that has been deserialized by simpl into json.
function simplDeserialize(json_string)
{
    console_message("simplDeserialize",3);
    console_message("simplDeserializeString:"+json_string,3);
	var total = 0;
	var name = "";
	var uncastObject = json_string;
	if(typeof json_string == "string")
		uncastObject = eval('('+json_string+')');
	
	for(var topLevel in uncastObject)
	{ 
		name = topLevel;
		console_message("name in top level is...",3);
		console_message(name,3);
		total+=1;
	}
	if(total != 1)
	{
		alert("Error. There should only be 1"+"top level name in this object.");
	}
	if(name == "object")
	{
	    //alert(JSON.stringify(json_string));
	    return json_string['object'];
	}
	else if(typeof eval(name) != "function")
	{
		alert("Error. "+name+" is not in your object definitions object.");
	}
	var contructor = (eval(''+name+''));
	return new contructor(uncastObject[name]);
}

function no_underscore_replacer(key, value)
{
  if (key[0]=="_")
  {
      return undefined;
  }
  else return value;
}

//Works just like JSON.stringify but ignores any fields with an underscore prefix
function simplStringify(obj)
{
    console_message("simplStringify",3);
    return JSON.stringify(obj, no_underscore_replacer);
}

//This function is needed to support simpl type maps.
function reformatObject(obj, unwrap) {
        console_message("reformatObject",3);
        if(unwrap)//Unwrap puts associative arrays back into Arrays.
        {
           var k = Array();
           for(var i in obj)
              k.push(reformatObject(obj[i]));
           return k;
        }
        var clone = {};
        for(var i in obj) {
            var type_inside = null;
            if(obj._simpl_map_types)
            {
               type_inside = obj._simpl_map_types[i];
            }
            if(typeof(obj[i])=="object")
            {
                if(type_inside)
                  clone[i] = reformatObject(obj[i],true);
                else
                  clone[i] = reformatObject(obj[i]);
            }
            else
                clone[i] = obj[i];
        }
        return clone;   
    }

//This serialized object.
function simplSerialize(simpl_object)//we could add something for this...
{
    console_message("simplSerialize",3);
    var return_object = new Object();
    return_object[""+(simpl_object._simpl_object_name)] = simpl_object;
    return_object = reformatObject(return_object);
	return simplStringify(return_object);
}


//This function makes maps and collections.
function castCollectionOrMap(ownerObject,deserializedCollection,item_key)
{
  console_message("castCollectionOrMap",3);
  var top_name = null;
  for(var sub in deserializedCollection)
    top_name = sub;
  var innerItems = deserializedCollection[sub];
  var collection = Array();
  var map = {};
  var innerItemType = ownerObject._simpl_collection_types[top_name];
  var is_map = false;
  var is_composite = false;
  if(!innerItemType)
  {
    is_map = true;
    innerItemType = ownerObject._simpl_map_types[top_name];
    console_message(top_name,3);
    if(!innerItemType)
    {//gb probably broked...
       console_message("ownerobject is",3);
       console_message(ownerObject,3);
       console_message("top object don't break is",3);
       console_message(top_name,3);
       console_message(item_key,3);
       var compositeType = ownerObject._simpl_composite_types[top_name];
       if(compositeType)
       {
          console_message("congrats on finding this composite object",3);
          is_composite = true;
       }
       else if(item_key == "object")
       {
          console_message("item_key is an object",3);
       }
       else if(ownerObject[top_name]['simpl.ref'])
       {
         alert("This is a reference object.");//this probably does not go here...
        }
        else
       {
  	       alert("This should be a map or a collection but it isn't.");
       }
      
    }
  }
  for(var sub in innerItems)
  {
    var item = {};
    if(innerItemType)//for collections of composites
        item[innerItemType] = innerItems[sub];
    else
        item = innerItems[sub];
    {
      if(is_composite)
      {
           console_message("just a composite so I did nothing with",3);
           console_message(item,3);
      }
      else
      {
        var item =  simplDeserialize(item);
        if(is_map)//need to check for object type... for which we have a special case of associative arrays that have keys that are the same as their value
        {
          if(is_map)
          {
             console_message("Found something to put in this map.. from the item",3);
              console_message(item_key,3)
             //console_message(item,3);//need to replace stuff here
             ownerObject[''];
             //map[item[item._map_key]] = item;
             map[item[item_key]] = item;
          }
        }
        else
          collection.push(item);
        }
     }
  }
  if(is_map)
    return map;
  else
    return collection;
}

//json constuct works as a constructor for simpl objects from json.  This is called from simplDeserialize
function jsonConstruct(json,target_object)
{
     console_message("jsonConstruct",3);
     target_object['_simpl_class_descriptor'] = addOrGetFromClassDescriptorMap(target_object['_simpl_object_name'],target_object['_simpl_class_descriptor']);

     
     for(field in json)
     {
        console_message("construct field is:"+field,3);
        if(typeof json[field] != "object")
        {
            if(field == "simpl.id")
            {
               console_message("simpl.id is",3);
               console_message(json[field],3);
               _simpl_ref_index_map[json[field]] = target_object;
               target_object["_simpl_id"] = json[field];
            }
            else
            {
              target_object[field] = json[field];
            }
        }
        else
        {
            var collection = {};
            collection[field] = json[field];
            if(collection[field]['simpl.ref'])
            {
               console_message("wait, we should do something different with this..",3);
               console_message(collection[field]['simpl.ref'],3);
               console_message("and I hope that was the number",3);
               //null may be needed here...
               target_object[field] = _simpl_ref_index_map[collection[field]['simpl.ref']];
            }
            else
            {
            console_message("field of the collection or map",3);
            console_message(field,3);
            var item_key = target_object['_simpl_map_types_keys'][field];
            target_object[field] = castCollectionOrMap(target_object,collection,item_key);
            }
        }
     }
     //check on type of class descriptor
     //target_object['_simpl_class_descriptor'] = simplDeserialize(target_object['_simpl_class_descriptor']);
}