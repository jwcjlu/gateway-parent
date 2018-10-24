local service_key = KEYS[1]
local capacity = tonumber(ARGV[1])
local last_value = tonumber(redis.call("get",service_key))
if last_value == nil then
  last_value = 0
end
local allowed = capacity-last_value
if allowed > 0 then
  redis.call("incr",service_key)
  redis.call("expire",service_key,120)
end
return {allowed,last_value}
