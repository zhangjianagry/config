import Mock from 'mockjs';
const LoginUsers = [
  {
    id: 1,
    username: 'admin',
    password: '123456',
    avatar: 'https://raw.githubusercontent.com/taylorchen709/markdown-images/master/vueadmin/user.png',
    name: 'admin'
  }
];

const Services = [];

for (let i = 0; i < 5; i++) {
    Services.push(Mock.mock({
    id: Mock.Random.guid(),
    serviceID: Mock.Random.guid(),
    addr: Mock.Random.url(),
    name: Mock.Random.name(),
    time: Mock.Random.date(),
        configs:[{configName:'id1',key:1,val:2,internal:'iii',modify_time:'1996-6-13',attr1:'aaa',attr2:'bbb'},{configName:'id2',key:1,val:2,internal:'iii',modify_time:'ddd',attr1:'aaa',attr2:'bbb'}]
  }));
}

export { LoginUsers, Services };
