//This function takes a string that has been deserialized by simpl into json.
function simplDeserialize(json_string)
{
	var total = 0;
	var name = "";
	var uncastObject = json_string;
	if(typeof json_string == "string")
		uncastObject = eval('('+json_string+')');
	
	for(var topLevel in uncastObject)
	{ 
		name = topLevel;
		total+=1;
	}
	if(total != 1)
	{
		alert("Error. There should only be 1"+"top level name in this object.");
	}
	
	if(typeof eval(name) != "function")
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
    return JSON.stringify(obj, no_underscore_replacer);
}

//This function is needed to support simpl type maps.
function reformatObject(obj, unwrap) {
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
    var return_object = new Object();
    return_object[""+(simpl_object._simpl_object_name)] = simpl_object;
    return_object = reformatObject(return_object);
	return simplStringify(return_object);
}


//This function makes maps and collections.
function castCollectionOrMap(ownerObject,deserializedCollection)
{
  var top_name = null;
  for(var sub in deserializedCollection)
    top_name = sub;
  var innerItems = deserializedCollection[sub];
  var collection = Array();
  var map = {};
  var innerItemType = ownerObject._simpl_collection_types[top_name];
  var is_map = false;
  if(!innerItemType)
  {
    is_map = true;
    innerItemType = ownerObject._simpl_map_types[top_name];
    if(!innerItemType)
      alert("This should be a map or a collection but it isn't.");
  }
  for(var sub in innerItems)
  {
    var item = {};
    if(innerItemType)//for collections of composites
        item[innerItemType] = innerItems[sub];
    else
        item = innerItems[sub];
    {
      var item =  simplDeserialize(item);
      if(is_map)
        map[item[item._map_key]] = item;
      else
        collection.push(item);
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
     for(field in json)
     {
        if(typeof json[field] != "object")
        {
            target_object[field] = json[field];
        }
        else
        {
            var collection = {};
            collection[field] = json[field];            
            target_object[field] = castCollectionOrMap(target_object,collection);
        }
     }
}