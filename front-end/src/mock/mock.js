import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';
import { LoginUsers, Services } from './data/data';
let _Services = Services;

export default {
  /**
   * mock bootstrap
   */
  bootstrap() {
    let mock = new MockAdapter(axios);

    // mock success request
    mock.onGet('/success').reply(200, {
      msg: 'success'
    });

    // mock error request
    mock.onGet('/error').reply(500, {
      msg: 'failure'
    });

    //登录
    mock.onPost('/login').reply(config => {
      let {username, password} = JSON.parse(config.data);
      return new Promise((resolve, reject) => {
        let user = null;
        setTimeout(() => {
          let hasUser = LoginUsers.some(u => {
            if (u.username === username && u.password === password) {
              user = JSON.parse(JSON.stringify(u));
              user.password = undefined;
              return true;
            }
          });

          if (hasUser) {
            resolve([200, { code: 200, msg: '请求成功', user }]);
          } else {
            resolve([200, { code: 500, msg: '账号或密码错误' }]);
          }
        }, 1000);
      });
    });


    //获取服务列表
    mock.onGet('/serv/listpage').reply(config => {
      let {page, name} = config.params;
      let mockServs = _Services.filter(serv => {
        if (name && serv.name.indexOf(name) == -1) return false;
        return true;
      });
      let total = mockServs.length;
      mockServs = mockServs.filter((u, index) => index < 20 * page && index >= 20 * (page - 1));
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          resolve([200, {
            total: total,
            servs: mockServs
          }]);
        }, 1000);
      });
    });

    //删除服务
    mock.onGet('/serv/remove').reply(config => {
      let { id } = config.params;
      _Services = _Services.filter(u => u.id !== id);
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          resolve([200, {
            code: 200,
            msg: '删除成功'
          }]);
        }, 500);
      });
    });

    //批量删除服务
    mock.onGet('/serv/batchremove').reply(config => {
      let { ids } = config.params;
      ids = ids.split(',');
      _Services = _Services.filter(u => !ids.includes(u.id));
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          resolve([200, {
            code: 200,
            msg: '删除成功'
          }]);
        }, 500);
      });
    });

    //编辑服务
    mock.onGet('/serv/edit').reply(config => {
      let { id, serviceID, addr, name, time } = config.params;
      _Services.some(u => {
        if (u.id === id) {
          u.serviceID = serviceID;
          u.addr = addr;
          u.name = name;
          u.time = time;
          return true;
        }
      });
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          resolve([200, {
            code: 200,
            msg: '编辑成功'
          }]);
        }, 500);
      });
    });

    //新增服务
    mock.onGet('/serv/add').reply(config => {
      let { name, addr, serviceID, time } = config.params;
      _Services.push({
        name: name,
        addr: addr,
          serviceID: serviceID,
        time: time,
      });
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          resolve([200, {
            code: 200,
            msg: '新增成功'
          }]);
        }, 500);
      });
    });

  }
};
