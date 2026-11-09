import { useEffect, useState } from 'react';
import { NavLink, Routes, Route, useNavigate, useLocation } from 'react-router-dom';
import { Wrench, LayoutDashboard, PlusCircle, ClipboardList, History, Users, RotateCcw } from 'lucide-react';
import { getRequests, getStats, createRequest, updateStatus, deleteRequest, getStaff, assignStaff, addStaff } from './api';

const demo = [
  { id: 4, ticketNumber:'MR-1004', reporterName:'siddu', roomNumber:'610', category:'Cleaning', priority:'HIGH', description:'Some stains on the floor', status:'IN_PROGRESS', createdAt:'2026-11-09T11:56:00' },
  { id: 3, ticketNumber:'MR-1003', reporterName:'Isha Gupta', roomNumber:'B-307', category:'Carpentry', priority:'LOW', description:'Study table drawer handle is loose.', status:'RESOLVED', createdAt:'2026-11-07T10:05:00' },
  { id: 2, ticketNumber:'MR-1002', reporterName:'Rahul Verma', roomNumber:'C-116', category:'Plumbing', priority:'EMERGENCY', description:'Water is leaking continuously from the bathroom tap.', status:'IN_PROGRESS', createdAt:'2026-11-08T11:45:00' },
  { id: 1, ticketNumber:'MR-1001', reporterName:'Karthik', roomNumber:'A-203', category:'Electrical', priority:'HIGH', description:'Ceiling fan is making unusual noise.', status:'ASSIGNED', createdAt:'2026-11-06T09:30:00' }
];
const fmt = v => v ? new Date(v).toLocaleString('en-IN', { dateStyle:'medium', timeStyle:'short' }) : '—';
const label = s => (s || '').replaceAll('_',' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase());

function App() {
  const [requests, setRequests] = useState([]);
  const [stats, setStats] = useState({ total:0, open:0, inProgress:0, resolved:0 });
  const [staff, setStaff] = useState([]);
  const [notice, setNotice] = useState('');
  const location = useLocation();
  const navigate = useNavigate();

  async function refresh() {
    try {
      const [r, s, people] = await Promise.all([getRequests(), getStats(), getStaff()]);
      setRequests(r); setStats(s); setStaff(people);
    } catch {
      const saved = localStorage.getItem('hostelfix-demo');
      const list = saved ? JSON.parse(saved) : demo;
      setRequests(list);
      setStats({ total:list.length, open:list.filter(x=>x.status==='OPEN').length, inProgress:list.filter(x=>['IN_PROGRESS','ASSIGNED'].includes(x.status)).length, resolved:list.filter(x=>x.status==='RESOLVED').length });
      setStaff([]);
    }
  }
  useEffect(() => { refresh(); }, []);
  useEffect(() => { setNotice(''); }, [location.pathname]);

  function localSave(list) {
    localStorage.setItem('hostelfix-demo', JSON.stringify(list));
    setRequests(list);
    setStats({ total:list.length, open:list.filter(x=>x.status==='OPEN').length, inProgress:list.filter(x=>['IN_PROGRESS','ASSIGNED'].includes(x.status)).length, resolved:list.filter(x=>x.status==='RESOLVED').length });
  }
  async function submit(data) {
    try { await createRequest(data); await refresh(); }
    catch {
      const list = JSON.parse(localStorage.getItem('hostelfix-demo') || JSON.stringify(demo));
      const id = Math.max(0, ...list.map(x=>x.id || 0)) + 1;
      list.unshift({ ...data, id, ticketNumber:`MR-${1000+id}`, status:'OPEN', createdAt:new Date().toISOString() });
      localSave(list);
    }
    setNotice('Maintenance request submitted successfully.');
    navigate('/requests');
  }
  async function statusChange(id, status) {
    try { await updateStatus(id, status); await refresh(); }
    catch { localSave(requests.map(x=>x.id===id ? {...x,status} : x)); }
  }
  async function remove(id) {
    if (!confirm('Delete this maintenance request?')) return;
    try { await deleteRequest(id); await refresh(); }
    catch { localSave(requests.filter(x=>x.id!==id)); }
  }
  async function assign(id, staffId) {
    try { await assignStaff(id, staffId); await refresh(); }
    catch { setNotice('Staff assignment requires the backend and database to be running.'); }
  }
  async function reset() {
    if (!confirm('Reset browser demo data?')) return;
    localStorage.removeItem('hostelfix-demo'); await refresh();
    setNotice('Browser demo data reset.');
  }

  return <div className="app-shell">
    <header className="topbar">
      <div className="brand"><div className="eyebrow">HOSTEL OPERATIONS</div><h1><Wrench size={22}/> HostelFix Portal</h1></div>
      <button className="reset-btn" onClick={reset}><RotateCcw size={16}/> Reset demo data</button>
    </header>
    <div className="layout">
      <aside className="sidebar">
        <nav>
          <NavLink to="/" end><LayoutDashboard/>Dashboard</NavLink>
          <NavLink to="/new"><PlusCircle/>New request</NavLink>
          <NavLink to="/requests"><ClipboardList/>All requests</NavLink>
          <NavLink to="/history"><History/>Service history</NavLink>
          <NavLink to="/staff"><Users/>Staff management</NavLink>
        </nav>
        <p className="side-note">Requests are stored in MySQL when the backend is running. Browser demo mode is used otherwise.</p>
      </aside>
      <main className="main">
        {notice && <div className="notice">{notice}</div>}
        <Routes>
          <Route path="/" element={<Dashboard stats={stats} requests={requests} goNew={()=>navigate('/new')} goAll={()=>navigate('/requests')}/>} />
          <Route path="/new" element={<RequestForm onSubmit={submit}/>} />
          <Route path="/requests" element={<RequestList requests={requests} staff={staff} onStatus={statusChange} onDelete={remove} onAssign={assign}/>} />
          <Route path="/history" element={<RequestList requests={requests.filter(x=>x.status==='RESOLVED')} staff={staff} history onStatus={statusChange} onDelete={remove} onAssign={assign}/>} />
          <Route path="/staff" element={<StaffPage staff={staff} onAdd={async d=>{try{await addStaff(d); await refresh();}catch{setNotice('Start the backend to save staff members.')}}}/>} />
          <Route path="*" element={<div className="panel">Page not found.</div>} />
        </Routes>
      </main>
    </div>
  </div>;
}

function PageHeading({kicker,title,action}) {
  return <div className="page-heading"><div><div className="eyebrow blue">{kicker}</div><h2>{title}</h2></div>{action}</div>;
}
function Dashboard({stats,requests,goNew,goAll}) {
  return <>
    <PageHeading kicker="OVERVIEW" title="Maintenance dashboard" action={<button className="primary" onClick={goNew}>+ Record request</button>}/>
    <div className="stats-grid">
      <Stat title="Total requests" value={stats.total}/><Stat title="Open requests" value={stats.open}/><Stat title="In progress" value={stats.inProgress}/><Stat title="Resolved" value={stats.resolved}/>
    </div>
    <section className="panel recent">
      <div className="panel-head"><h3>Recent requests</h3><button className="link-btn" onClick={goAll}>View all</button></div>
      {requests.slice(0,5).map(r=><RequestRow key={r.id} item={r}/>)}
      {!requests.length && <p className="muted">No requests found. Create the first maintenance request.</p>}
    </section>
  </>;
}
function Stat({title,value}) { return <div className="stat-card"><span>{title}</span><strong>{value}</strong></div>; }
function RequestRow({item}) {
  return <div className="request-row">
    <div className="request-main"><strong>{item.ticketNumber} · Room {item.roomNumber}</strong><p>{item.category} - {item.description}</p><small>{item.reporterName} · {fmt(item.createdAt)}</small></div>
    <div className="pills"><span className={`pill priority-${item.priority?.toLowerCase()}`}>{label(item.priority)}</span><span className={`pill status-${item.status?.toLowerCase()}`}>{label(item.status)}</span></div>
  </div>;
}
function RequestForm({onSubmit}) {
  const [form,setForm] = useState({ reporterName:'', roomNumber:'', category:'Electrical', priority:'Medium', description:'' });
  const [error,setError] = useState('');
  const change = e => setForm({...form,[e.target.name]:e.target.value});
  function send(e) { e.preventDefault(); if (!form.reporterName.trim() || !form.roomNumber.trim() || !form.description.trim()) {setError('Please complete all fields.');return;} setError(''); onSubmit(form); }
  return <>
    <PageHeading kicker="STUDENT / WARDEN" title="Record a maintenance request"/>
    <form className="panel form-panel" onSubmit={send}>
      <div className="form-grid">
        <Field label="Student / reporter name"><input name="reporterName" value={form.reporterName} onChange={change} placeholder="e.g., Ananya Sharma" required maxLength="100"/></Field>
        <Field label="Room number"><input name="roomNumber" value={form.roomNumber} onChange={change} placeholder="e.g., B-214" required maxLength="30"/></Field>
        <Field label="Category"><select name="category" value={form.category} onChange={change}>{['Electrical','Plumbing','Carpentry','Cleaning','Internet','Other'].map(x=><option key={x}>{x}</option>)}</select></Field>
        <Field label="Priority"><select name="priority" value={form.priority} onChange={change}>{['Low','Medium','High','Emergency'].map(x=><option key={x}>{x}</option>)}</select></Field>
      </div>
      <Field label="Problem description"><textarea name="description" value={form.description} onChange={change} placeholder="Describe the problem, location, and any safety concern." rows="5" required maxLength="2000"/></Field>
      {error && <p className="error">{error}</p>}
      <button className="primary submit-btn">Submit maintenance request</button>
    </form>
  </>;
}
function Field({label,children}) { return <label className="field"><span>{label}</span>{children}</label>; }
function RequestList({requests,staff,onStatus,onDelete,onAssign,history=false}) {
  const [query,setQuery] = useState(''); const [status,setStatus] = useState('ALL');
  const filtered = requests.filter(r => `${r.ticketNumber} ${r.reporterName} ${r.roomNumber} ${r.category} ${r.description}`.toLowerCase().includes(query.toLowerCase()) && (status==='ALL' || r.status===status));
  return <>
    <PageHeading kicker={history?'COMPLETED WORK':'REQUEST MANAGEMENT'} title={history?'Service history':'All maintenance requests'}/>
    <section className="panel">
      <div className="toolbar"><input value={query} onChange={e=>setQuery(e.target.value)} placeholder="Search ticket, student, room or issue..."/><select value={status} onChange={e=>setStatus(e.target.value)}><option value="ALL">All statuses</option>{['OPEN','ASSIGNED','IN_PROGRESS','RESOLVED','CLOSED'].map(s=><option value={s} key={s}>{label(s)}</option>)}</select></div>
      <div className="table-wrap"><table><thead><tr><th>Ticket / Room</th><th>Issue</th><th>Priority</th><th>Status</th><th>Assign staff</th><th>Update</th><th></th></tr></thead>
      <tbody>{filtered.map(r=><tr key={r.id}><td><b>{r.ticketNumber}</b><small>Room {r.roomNumber}<br/>{r.reporterName}</small></td><td>{r.category}<small>{r.description}</small></td><td><span className={`pill priority-${r.priority?.toLowerCase()}`}>{label(r.priority)}</span></td><td><span className={`pill status-${r.status?.toLowerCase()}`}>{label(r.status)}</span></td><td><select defaultValue="" onChange={e=>e.target.value && onAssign(r.id,e.target.value)}><option value="">Assign...</option>{staff.map(s=><option value={s.id} key={s.id}>{s.name}</option>)}</select></td><td><select value={r.status} onChange={e=>onStatus(r.id,e.target.value)}>{['OPEN','ASSIGNED','IN_PROGRESS','RESOLVED','CLOSED'].map(s=><option key={s}>{s}</option>)}</select></td><td><button className="delete-btn" onClick={()=>onDelete(r.id)}>Delete</button></td></tr>)}</tbody></table></div>
      {!filtered.length && <p className="muted">No matching requests.</p>}
    </section>
  </>;
}
function StaffPage({staff,onAdd}) {
  const [form,setForm] = useState({name:'',department:'Electrical',phone:''});
  return <>
    <PageHeading kicker="HOSTEL TEAM" title="Staff management"/>
    <section className="panel">
      <h3>Add maintenance staff</h3>
      <form className="staff-form" onSubmit={e=>{e.preventDefault();if(form.name.trim()){onAdd(form);setForm({name:'',department:'Electrical',phone:''});}}}>
        <input placeholder="Staff name" required value={form.name} onChange={e=>setForm({...form,name:e.target.value})}/>
        <select value={form.department} onChange={e=>setForm({...form,department:e.target.value})}>{['Electrical','Plumbing','Carpentry','Cleaning','General'].map(x=><option key={x}>{x}</option>)}</select>
        <input placeholder="Phone (optional)" value={form.phone} onChange={e=>setForm({...form,phone:e.target.value})}/>
        <button className="primary">Add staff</button>
      </form>
      <div className="staff-list">{staff.map(s=><div className="staff-item" key={s.id}><b>{s.name}</b><span>{s.department}</span><small>{s.phone || 'No phone added'}</small></div>)}</div>
      {!staff.length && <p className="muted">No staff records yet. Start the backend and add staff members.</p>}
    </section>
  </>;
}
export default App;
